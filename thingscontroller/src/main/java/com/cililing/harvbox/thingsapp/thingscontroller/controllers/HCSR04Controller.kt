package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.core.Clock
import com.cililing.harvbox.thingsapp.thingscontroller.core.Logger
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutor
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.io.Serializable
import java.util.concurrent.TimeUnit

interface HCSR04Controller : Controller<HCSR04Snapshot> {
    var distanceCallback: ((Double) -> Unit)?
}

data class HCSR04Snapshot(
        val value: Double?
): Serializable

internal class HCSR04ControllerImpl(
        gpioTrig: String,
        gpioEcho: String,
        override val parent: Controller<*>? = null
) : HCSR04Controller, KoinComponent {

    companion object {
        private const val TRIGGERS_INTERVAL: Long = 300
    }

    private val peripheralManager by inject<PeripheralManager>()
    private val logger by inject<Logger>()
    private val clock by inject<Clock>()
    private val executor by inject<SerialExecutor>()

    private val echoGpio = peripheralManager.openGpio(gpioEcho)
    private val trigGpio = peripheralManager.openGpio(gpioTrig)

    private var lastRead: Double? = null
    override var distanceCallback: ((Double) -> Unit)? = null

    private val triggerRunnable: Runnable = Runnable {
        try {
            var startTime: Long = 0
            var endTime: Long = 0

            Thread.sleep(10)
            logger.i("Trig: ${trigGpio.value}, Echo: ${echoGpio.value}")
            trigGpio.value = !trigGpio.value
            logger.i("Trig: ${trigGpio.value}, Echo: ${echoGpio.value}")
            Thread.sleep(0, 20_000)
            logger.i("Trig: ${trigGpio.value}, Echo: ${echoGpio.value}")
            trigGpio.value = !trigGpio.value
            logger.i("Trig: ${trigGpio.value}, Echo: ${echoGpio.value}")

            while (!echoGpio.value) {
                startTime = clock.nano()
            }
            logger.i("Trig: ${trigGpio.value}, Echo: ${echoGpio.value}")
            while (echoGpio.value) {
                endTime = clock.nano()
            }

            val ellapsedTime = TimeUnit.NANOSECONDS.toMicros(endTime - startTime)
            val distanceInCm = ellapsedTime / 58.0

            distanceCallback?.invoke(distanceInCm)
            lastRead = distanceInCm

        } catch (ex: IOException) {
            logger.e("Error on IO", exception = ex)
        }
    }

    init {
        with(echoGpio) {
            setDirection(Gpio.DIRECTION_IN)
            setEdgeTriggerType(Gpio.EDGE_BOTH)
            setActiveType(Gpio.ACTIVE_HIGH)
        }

        with(trigGpio) {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            setActiveType(Gpio.ACTIVE_HIGH)
            value = false
        }

        executor.startExecuting(TRIGGERS_INTERVAL, true, triggerRunnable)
    }

    override fun getSnapshot(): HCSR04Snapshot {
        return HCSR04Snapshot(lastRead)
    }

    override fun release() {
        executor.awaitTermination(0)
        try {
            echoGpio.close()
            trigGpio.close()
        } catch (ex: Exception) {
            logger.e("Error on GPIO", exception = ex)
        }
    }
}