package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import android.util.Log
import com.cililing.harvbox.thingsapp.common.TAG
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager

interface ButtonController : Controller {
    fun registerCallback(callback: () -> Boolean)
    fun unregisterCallback()
}

class ButtonControllerImpl(peripheralManager: PeripheralManager,
                           gpio: String) : ButtonController {

    private val buttonGpio: Gpio = peripheralManager.openGpio(gpio)
    private var gpioCallback: GpioCallback? = null

    init {
        buttonGpio.apply {
            setDirection(Gpio.DIRECTION_IN)
            setEdgeTriggerType(Gpio.EDGE_FALLING)
        }
    }

    override fun registerCallback(callback: () -> Boolean) {
        if (this.gpioCallback != null) {
            buttonGpio.unregisterGpioCallback(this.gpioCallback)
        }

        this.gpioCallback = GpioCallback {
            callback.invoke()
            true
        }

        buttonGpio.registerGpioCallback(gpioCallback)
    }

    override fun unregisterCallback() {
        if (gpioCallback != null) {
            buttonGpio.unregisterGpioCallback(gpioCallback)
        }
    }

    override fun release() {
        try {
            if (gpioCallback != null) {
                buttonGpio.unregisterGpioCallback(gpioCallback)
            }
            gpioCallback = null;
            buttonGpio.close()
        } catch (ex: Exception) {
            Log.e(TAG, "Error on PeripheralIO Api", ex)
        }
    }

}