package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelayController
import com.cililing.harvbox.thingsapp.thingscontroller.core.getKoinModule
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class ThingsControllerImpl internal constructor(
        private val configMap: ControllerConfigMap = ControllerConfigMapDefault,
        debug: Boolean = false,
        override val parent: Controller<*>? = null
) : ThingsController, KoinComponent {

    init {
        startKoin {
            if (debug) printLogger()

            modules(getKoinModule(debug))
        }
    }

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
            parentJob + Dispatchers.IO
    )

    val ads1015Controller by inject<ADS1015Controller> {
        parametersOf(configMap.adcI2C, configMap.adcAdrr, Ads1xxx.RANGE_4_096V, this)
    }

    val twoRelayController by inject<TwoRelayController> {
        parametersOf(configMap.relayIn1, configMap.relayIn2, this)
    }

    val proximityController by inject<HCSR04Controller> {
        parametersOf(configMap.proximitySensorTrig, configMap.proximitySensorEcho, this)
    }

    private val allControllers = listOf(
            ads1015Controller,
            twoRelayController,
            proximityController
    )

    override fun getSnapshot(): ThingsSnapshot {
        return ThingsSnapshot(
                twoRelaySnapshot = twoRelayController.getSnapshot(),
                proximitySnapshot = proximityController.getSnapshot(),
                ads1015Snapshot = ads1015Controller.getSnapshot()
        )
    }

    override suspend fun getSnapshotAsync(): ThingsSnapshot {
        return withContext(coroutineScope.coroutineContext) {
            delay(Random.nextLong(1000, 2000))
            getSnapshot()
        }
    }

    override fun release() {
        allControllers.forEach { it.release() }
        parentJob.cancel()
    }

}