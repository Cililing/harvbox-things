package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.core.Logger
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutor
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.lang.IllegalArgumentException

interface ADS1015Controller : Controller {
    interface ADS1015PinController : Controller {
        fun read(reader: (Int) -> Unit)
        fun runReading(reader: (Int) -> Unit)
        fun stopReading()
    }

    val a0: ADS1015PinController
    val a1: ADS1015PinController
    val a2: ADS1015PinController
    val a3: ADS1015PinController

    operator fun get(index: Int): ADS1015PinController {
        return when (index) {
            0 -> a0
            1 -> a1
            2 -> a2
            3 -> a3
            else -> throw IllegalArgumentException("Index invalid. Possible indexes: 0, 1, 2, 3")
        }
    }
}

internal class ADS1015ControllerImpl(i2cName: String,
                                     addr: Int,
                                     range: Int) : ADS1015Controller, KoinComponent {

    companion object {
        private const val EXECUTOR_THREADS = 4
    }

    private val logger by inject<Logger>()
    private val executor by inject<FixedExecutor> { parametersOf(EXECUTOR_THREADS) }
    private val instance: Ads1xxx = Ads1xxx(i2cName, addr, Ads1xxx.Configuration.ADS1015)

    private inner class ADS1015PinControllerImpl(val channel: Int) :
            ADS1015Controller.ADS1015PinController {
        override fun read(reader: (Int) -> Unit) {
            reader.invoke(instance.readSingleEndedInput(channel))
        }

        override fun release() {
            stopReading()
        }

        override fun runReading(reader: (Int) -> Unit) {
            executor.startExecuting(100, true) {
                read(reader)
            }
        }

        override fun stopReading() {
        }
    }

    override val a0: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(0)
    override val a1: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(1)
    override val a2: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(2)
    override val a3: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(3)

    init {
        instance.inputRange = range
    }

    override fun release() {
        executor.awaitTermination(0)
        instance.close()
    }
}