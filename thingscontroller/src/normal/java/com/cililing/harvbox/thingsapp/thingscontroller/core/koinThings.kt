package com.cililing.harvbox.thingsapp.thingscontroller.core

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.*
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015ControllerImpl
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04ControllerImpl
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelayControllerImpl
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateController
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateControllerImpl
import com.google.android.things.pio.PeripheralManager
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun getThingsModule(): Module {
    return module {
        single {
            PeripheralManager.getInstance() as PeripheralManager
        }

        // Controllers
        factory { (gpio1: String, startingState: Boolean, parent: Controller<*>?) ->
            StateControllerImpl(gpio1, startingState, parent) as StateController
        }

        factory { (gpio1: String, gpio2: String, parent: Controller<*>?) ->
            TwoRelayControllerImpl(gpio1, gpio2, parent) as TwoRelayController
        }

        factory { (gpioTrig: String, gpioEcho: String, parent: Controller<*>?) ->
            HCSR04ControllerImpl(gpioTrig, gpioEcho, parent) as HCSR04Controller
        }

        factory { (i2cName: String, addr: Int, range: Int,
                          mappers: Array<(Int?) -> Double?>, parent: Controller<*>?) ->
            ADS1015ControllerImpl(i2cName, addr, range, mappers, parent) as ADS1015Controller
        }

    }
}