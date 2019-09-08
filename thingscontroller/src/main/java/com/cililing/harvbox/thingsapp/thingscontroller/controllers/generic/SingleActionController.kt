package com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.core.Logger
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import org.koin.core.KoinComponent
import org.koin.core.inject

interface SingleActionController : Controller {
    fun registerCallback(callback: () -> Boolean)
    fun unregisterCallback()

    override fun release()
}

internal class SingleActionControllerImpl(gpio: String) : SingleActionController, KoinComponent {

    private val peripheralManager by inject<PeripheralManager>()
    private val logger by inject<Logger>()

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
        gpioCallback = null
    }

    override fun release() {
        try {
            if (gpioCallback != null) {
                buttonGpio.unregisterGpioCallback(gpioCallback)
            }
            gpioCallback = null
            buttonGpio.close()
        } catch (exception: Exception) {
            logger.e("Error on PeripheralIO Api, $buttonGpio", exception = exception)
        }
    }

}