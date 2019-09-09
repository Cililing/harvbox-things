package com.cililing.harvbox.thingsapp.thingscontroller.core

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.*
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015ControllerImpl
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateController
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateControllerImpl
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutor
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutorImpl
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutor
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutorImpl
import com.google.android.things.pio.PeripheralManager
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun getKoinModule(debug: Boolean): Module {
    return module {
        single {
            if (debug) DebugLogger() else ReleaseLogger() as Logger
        }

        single {
            PeripheralManager.getInstance() as PeripheralManager
        }

        factory {
            RealClock() as Clock
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

        factory { (i2cName: String, addr: Int, range: Int, parent: Controller<*>?) ->
            ADS1015ControllerImpl(i2cName, addr, range, parent) as ADS1015Controller
        }

        // Executors
        factory {
            SerialExecutorImpl() as SerialExecutor
        }

        factory { (poolSize: Int) ->
            FixedExecutorImpl(poolSize) as FixedExecutor
        }
    }
}