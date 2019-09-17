package com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.core.Logger
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException

internal class StateControllerImpl(
        gpio: String,
        startingState: Boolean = false,
        override val parent: Controller<*>? = null
) : StateController, KoinComponent {

    private val peripheralManager by inject<PeripheralManager>()
    private val logger by inject<Logger>()

    private val stateGpio = peripheralManager.openGpio(gpio)

    init {
        stateGpio.apply {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            value = startingState
        }
    }

    override fun getState(): Boolean {
        return stateGpio.value
    }

    override fun setState(value: Boolean) {
        stateGpio.value = value
    }

    override fun changeState() {
        stateGpio.value = !stateGpio.value
    }

    override fun release() {
        try {
            stateGpio.close()
        } catch (e: IOException) {
            logger.e("Error on: $stateGpio")
        }
    }

    override fun getSnapshot(): StateSnapshot {
        return StateSnapshot(
                gpioState = stateGpio.value
        )
    }
}