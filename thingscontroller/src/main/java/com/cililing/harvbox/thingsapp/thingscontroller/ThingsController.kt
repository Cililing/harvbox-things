package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.*
import com.cililing.harvbox.thingsapp.thingscontroller.core.getKoinModule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.io.Serializable

data class ThingsSnapshot(
        val twoRelaySnapshot: TwoRelaySnapshot,
        val proximitySnapshot: HCSR04Snapshot
): Serializable

class ThingsController internal constructor(
        private val configMap: ControllerConfigMap = ControllerConfigMapDefault,
        debug: Boolean = false,
        override val parent: Controller<*>? = null
) : Controller<ThingsSnapshot>, KoinComponent {

    init {
        startKoin {
            if (debug) printLogger()

            modules(getKoinModule(debug))
        }
    }

//    private val ads1015Controller by inject<ADS1015Controller> {
//        parametersOf(configMap.adcI2C, configMap.adcAdrr, Ads1xxx.RANGE_4_096V)
//    }
//
    val twoRelayController by inject<TwoRelayController> {
        parametersOf(configMap.relayIn1, configMap.relayIn2, this)
    }

    val proximityController by inject<HCSR04Controller> {
        parametersOf(configMap.proximitySensorTrig, configMap.proximitySensorEcho, this)
    }

//    internal val tempController by lazy {
//        ads1015Controller[configMap.tempSensorAdcPin]
//    }
//
//    internal val humidityController by lazy {
//        ads1015Controller[configMap.humiditySensorAdcPin]
//    }

    private val allControllers = listOf(
//            ads1015Controller,
            twoRelayController,
            proximityController
//            tempController,
//            humidityController
    )

    override fun getSnapshot(): ThingsSnapshot {
        return ThingsSnapshot(
                twoRelaySnapshot = twoRelayController.getSnapshot(),
                proximitySnapshot = proximityController.getSnapshot()
        )
    }

    override fun release() {
        allControllers.forEach { it.release() }
    }

}