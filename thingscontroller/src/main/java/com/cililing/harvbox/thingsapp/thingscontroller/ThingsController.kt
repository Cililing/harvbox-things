package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelayController
import com.cililing.harvbox.thingsapp.thingscontroller.core.getKoinModule
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class ThingsController internal constructor(
        private val configMap: ControllerConfigMap = ControllerConfigMapDefault,
        debug: Boolean = false
) : Controller, KoinComponent {

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
        parametersOf(configMap.relayIn1, configMap.relayIn2)
    }

    val proximityController by inject<HCSR04Controller> {
        parametersOf(configMap.proximitySensorTrig, configMap.proximitySensorEcho)
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

    override fun release() {
        allControllers.forEach { it.release() }
    }
}