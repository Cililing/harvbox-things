package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.core.Logger
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutor
import com.google.android.things.contrib.driver.adc.ads1xxx.Ads1xxx
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.io.Serializable
import java.lang.IllegalArgumentException

interface ADS1015Controller : Controller<ADS1015Snapshot> {
    interface ADS1015PinController : Controller<ADS1015PinSnapshot> {
        fun read(reader: (Int) -> Unit)
        fun runReading(reader: (Int) -> Unit)
        fun stopReading()
    }

    data class ADS1015PinSnapshot(
            val value: Int?
    ): Serializable

    val a0: ADS1015PinController
    val a1: ADS1015PinController
//    val a2: ADS1015PinController
//    val a3: ADS1015PinController

    operator fun get(index: Int): ADS1015PinController {
        return when (index) {
            0 -> a0
            1 -> a1
//            2 -> a2
//            3 -> a3
            else -> throw IllegalArgumentException("Index invalid. Possible indexes: 0, 1, 2, 3")
        }
    }
}

data class ADS1015Snapshot(
        private val a0: ADS1015Controller.ADS1015PinSnapshot,
        private val a1: ADS1015Controller.ADS1015PinSnapshot
//        private val a2: ADS1015Controller.ADS1015PinSnapshot,
//        private val a3: ADS1015Controller.ADS1015PinSnapshot
) : Serializable

internal class ADS1015ControllerImpl(i2cName: String,
                                     addr: Int,
                                     range: Int,
                                     override val parent: Controller<*>? = null) : ADS1015Controller, KoinComponent {
    companion object {
        private const val EXECUTOR_THREADS = 4
    }

    private val logger by inject<Logger>()
    private val executor by inject<FixedExecutor> { parametersOf(EXECUTOR_THREADS) }
    private val instance: Ads1xxx = Ads1xxx(i2cName, addr, Ads1xxx.Configuration.ADS1015)

    private inner class ADS1015PinControllerImpl(val channel: Int,
                                                 override val parent: Controller<*>? = this) :
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
            return ADS1015Controller.ADS1015PinSnapshot(lastRead)
        }
    }

    override val a0: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(0)
    override val a1: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(1)
//    override val a2: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(2)
//    override val a3: ADS1015Controller.ADS1015PinController = ADS1015PinControllerImpl(3)

    init {
        instance.inputRange = range
    }

    override fun getSnapshot(): ADS1015Snapshot {
        return ADS1015Snapshot(
                a0.getSnapshot(),
                a1.getSnapshot()
//                a2.getSnapshot(),
//                a3.getSnapshot()
        )
    }

    override fun release() {
        executor.awaitTermination(0)
        instance.close()
    }
}