package com.dev.t_rex.thingscontroller.controllers

import android.util.Log
import com.cililing.common.TAG
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import java.io.IOException

interface LEDController : Controller {
    fun getState(): Boolean
    fun setState(value: Boolean)
    fun changeState()
}

class LEDControllerImpl(peripheralManager: PeripheralManager,
                        gpio: String) : LEDController {
    private val ledGpio = peripheralManager.openGpio(gpio)

    init {
        ledGpio.apply {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        }
    }

    override fun getState(): Boolean {
        return ledGpio.value
    }

    override fun setState(value: Boolean) {
        ledGpio.value = value
    }

    override fun changeState() {
        ledGpio.value = !ledGpio.value
    }

    override fun release() {
        try {
            ledGpio.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error on peripheralIO API")
        }
    }
}