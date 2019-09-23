package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.thingsapp.thingscontroller.core.StandaloneKoinCompontent
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutor
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

internal class ADS1015ControllerImpl(
    i2cName: String,
    addr: Int,
    range: Int,
    private val valueMappers: Array<(Int?) -> Double?>,
    override val parent: Controller<*>? = null
) : ADS1015Controller, StandaloneKoinCompontent {
    companion object {
        private const val EXECUTOR_THREADS = 4
    }

    private val logger by inject<Logger>()
    private val executor by inject<FixedExecutor> { parametersOf(EXECUTOR_THREADS) }
    private val instance: Ads1xxx = Ads1xxx(i2cName, addr, Ads1xxx.Configuration.ADS1015)

    private inner class ADS1015PinControllerImpl(
        val channel: Int,
        override val parent: Controller<*>? = this
    ) :
            ADS1015Controller.ADS1015PinController {
        private var lastRead: Int? = null

        override fun read(reader: (Int) -> Unit) {
            instance.readSingleEndedInput(channel).also {
                reader.invoke(it)
                lastRead = it
            }
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

        override fun getSnapshot(): ADS1015Controller.ADS1015PinSnapshot {
            return ADS1015Controller.ADS1015PinSnapshot(valueMappers[channel].invoke(lastRead))
        }
    }

    override val a0: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(0)
    override val a1: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(1)

    init {
        instance.inputRange = range
    }

    override fun getSnapshot(): ADS1015Snapshot {
        return ADS1015Snapshot(
                a0.getSnapshot(),
                a1.getSnapshot()
        )
    }

    override fun release() {
        executor.awaitTermination(0)
        instance.close()
    }
}