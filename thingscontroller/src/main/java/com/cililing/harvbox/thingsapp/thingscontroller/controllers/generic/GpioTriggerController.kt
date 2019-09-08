package com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.core.Logger
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import org.koin.core.KoinComponent
import org.koin.core.inject

interface GpioTriggerController : Controller {
    fun setCallback(onTriggerChange: (newState: Boolean) -> Unit)
    fun unregisterCallback()

    override fun release()
}

internal class GpioTriggerControllerImpl(gpio: String) : GpioTriggerController, KoinComponent {

    private val peripheralManager by inject<PeripheralManager>()
    private val logger by inject<Logger>()

    private val triggerGpio = peripheralManager.openGpio(gpio)
    private var gpioCallback: GpioCallback? = null

    init {
        triggerGpio.apply {
            setDirection(Gpio.EDGE_BOTH)
        }
    }

    override fun setCallback(onTriggerChange: (newState: Boolean) -> Unit) {
        if (gpioCallback != null) {
            triggerGpio.unregisterGpioCallback(gpioCallback)
        }

        gpioCallback = GpioCallback {
            onTriggerChange.invoke(it.value)
            true
        }

        triggerGpio.registerGpioCallback(gpioCallback)
    }

    override fun unregisterCallback() {
        if (gpioCallback != null) {
            triggerGpio.unregisterGpioCallback(gpioCallback)
            gpioCallback = null
        }
    }

    override fun release() {
        try {
            if (gpioCallback != null) {
                triggerGpio.unregisterGpioCallback(gpioCallback)
                gpioCallback = null
                triggerGpio.close()
            }
        } catch (exception: Exception) {
            logger.e("Error on PeripheralIO Api: $triggerGpio", exception = exception)
        }
    }

}