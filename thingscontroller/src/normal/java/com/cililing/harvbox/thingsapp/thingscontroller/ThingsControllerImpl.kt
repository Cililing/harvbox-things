package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelayController
import com.cililing.harvbox.thingsapp.thingscontroller.core.StandaloneKoinCompontent
import com.cililing.harvbox.thingsapp.thingscontroller.core.StandaloneKoinContext
import com.cililing.harvbox.thingsapp.thingscontroller.core.getKoinModule
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication

class ThingsControllerImpl internal constructor(
        private val configMap: ControllerConfigMap = ControllerConfigMapDefault,
        debug: Boolean = false,
        override val parent: Controller<*>? = null
) : ThingsController, StandaloneKoinCompontent {

    init {
        StandaloneKoinContext.koinApplication = koinApplication {
            if (debug) printLogger()
            modules(getKoinModule(debug))
        }
    }

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

    override fun setState(state: RequiredThingsState) {
        twoRelayController.relay1.setState(state.relay1)
        twoRelayController.relay2.setState(state.relay2)
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
    }

}