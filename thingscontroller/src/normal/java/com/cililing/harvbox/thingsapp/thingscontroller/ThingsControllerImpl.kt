package com.cililing.harvbox.thingsapp.thingscontroller

import android.content.Context
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelayController
import com.cililing.harvbox.thingsapp.thingscontroller.core.CameraAdapter
import com.cililing.harvbox.thingsapp.thingscontroller.core.StandaloneKoinCompontent
import com.cililing.harvbox.thingsapp.thingscontroller.core.StandaloneKoinContext
import com.cililing.harvbox.thingsapp.thingscontroller.core.getKoinModule
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication

class ThingsControllerImpl internal constructor(
    context: Context,
    private val configMap: ControllerConfigMap = ControllerConfigMapDefault,
    override val photoListener: (ByteArray) -> Unit,
    debug: Boolean = false,
    override val parent: Controller<*>? = null
) : ThingsController, StandaloneKoinCompontent {

    init {
        StandaloneKoinContext.koinApplication = koinApplication {
            if (debug) printLogger()
            modules(getKoinModule(debug))
        }
    }

    private val cameraAdapter = CameraAdapter
    private val cameraThread = HandlerThread("CameraBackground").apply {
        start()
    }
    private val cameraHandler = Handler(cameraThread.looper)

    private val imageAvailableListener = ImageReader.OnImageAvailableListener {
        val image = it.acquireLatestImage()
        val buffer = image.planes[0].buffer
        val imageBytes = ByteArray(buffer.remaining())

        image.close()
        photoListener.invoke(imageBytes)
    }

    init {
        cameraAdapter.initialize(
            context,
            cameraHandler,
            imageAvailableListener
        )
    }

    override fun requestPhoto(): Boolean {
        return cameraAdapter.takePicture()
    }

    private val ads1015Mappers: Array<(Int?) -> Double?> = arrayOf(
        { i -> i?.toDouble() },
        { i -> i?.toDouble() }
    )

    private val ads1015Controller by inject<ADS1015Controller> {
        parametersOf(configMap.adcI2C,
            configMap.adcAdrr,
            Ads1xxx.RANGE_4_096V,
            ads1015Mappers,
            this
        )
    }

    private val twoRelayController by inject<TwoRelayController> {
        parametersOf(configMap.relayIn1, configMap.relayIn2, this)
    }

    // TODO: make private
    val proximityController by inject<HCSR04Controller> {
        parametersOf(configMap.proximitySensorTrig, configMap.proximitySensorEcho, this)
    }

    private val allControllers = listOf(
        ads1015Controller,
        twoRelayController,
        proximityController
    )

    override fun setState(state: RequiredThingsState) {
        twoRelayController.relay1.setState(state.relay1)
        twoRelayController.relay2.setState(state.relay2)
    }

    override fun setState(relay1: Boolean?, relay2: Boolean?) {
        relay1?.let { twoRelayController.relay1.setState(it) }
        relay2?.let { twoRelayController.relay2.setState(it) }
    }

    override fun getSnapshot(): ThingsSnapshot {
        return ThingsSnapshot(
            twoRelaySnapshot = twoRelayController.getSnapshot(),
            proximitySnapshot = proximityController.getSnapshot(),
            ads1015Snapshot = ads1015Controller.getSnapshot()
        )
    }

    override fun release() {
        allControllers.forEach { it.release() }
        cameraAdapter.shutDown()
    }
}