package com.dev.t_rex.harvbox.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.cililing.common.TAG
import com.dev.t_rex.harvbox.R
import com.dev.t_rex.thingscontroller.controllers.ButtonController
import com.dev.t_rex.thingscontroller.controllers.ButtonControllerImpl
import com.dev.t_rex.thingscontroller.controllers.LEDController
import com.dev.t_rex.thingscontroller.controllers.LEDControllerImpl
import com.dev.t_rex.thingscontroller.core.I2CHelper
import com.dev.t_rex.thingscontroller.core.PeripheralManagerProvider
import com.google.android.things.pio.PeripheralManager

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    private val ledController: LEDController by lazy {
        LEDControllerImpl(PeripheralManagerProvider.getInstance(), "GPIO6_IO15")
    }

    private val buttonController: ButtonController by lazy {
        ButtonControllerImpl(PeripheralManager.getInstance(), "GPIO6_IO14")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLed()
        buttonController.registerCallback {
            Log.d(TAG, "Button is working ahwuuu")

            val ledState = ledController.getState()
            ledController.setState(!ledState)

            true
        }

        Log.d(this.javaClass.simpleName, I2CHelper.getDeviceList(PeripheralManagerProvider.getInstance()).toString())
    }

    override fun onDestroy() {
        ledController.release()
        buttonController.release()

        super.onDestroy()
    }

    private fun startLed() {
        ledController.setState(true)
    }

    private fun stopLed() {
        ledController.setState(false)
    }
}
