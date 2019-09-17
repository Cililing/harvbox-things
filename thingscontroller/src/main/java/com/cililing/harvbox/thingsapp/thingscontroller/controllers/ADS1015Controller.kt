package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import java.io.Serializable

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

    operator fun get(index: Int): ADS1015PinController {
        return when (index) {
            0 -> a0
            1 -> a1
            else -> throw IllegalArgumentException("Index invalid. Possible indexes: 0, 1, 2, 3")
        }
    }
}

