package com.cililing.harvbox.thingsapp.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.widget.CompoundButton
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.thingscontroller.ControllerConfigMapDefault
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsController
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsControllerBuilder
import com.cililing.harvbox.thingsapp.views.SingleRelayView

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

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val thingsController: ThingsController = ThingsControllerBuilder()
                .isDebug(true)
                .configMap(ControllerConfigMapDefault)
                .withProximityListener {
                    println("Trig State change to $it")
                }
                .build()

        val relayController = thingsController.twoRelayController

        val r1: SingleRelayView = findViewById(R.id.relay_1)
        val r2: SingleRelayView = findViewById(R.id.relay_2)

        r1.labelText = "___1___"
        r2.labelText = "___2___"

        r1.isChecked = relayController.relay1.getState()
        r2.isChecked = relayController.relay2.getState()

        r1.setOnCheckedChangeListener(
                CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    relayController.relay1.changeState()
                })
        r2.setOnCheckedChangeListener(
                CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    relayController.relay2.changeState()
                }
        )
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)

        super.onDestroy()
    }
}
