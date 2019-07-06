package com.dev.t_rex.thingscontroller.core

import android.util.Log
import com.google.android.things.pio.PeripheralManager

object PeripheralManagerProvider {

    private val manager by lazy {
        Log.d(this.javaClass.simpleName, "PeripheralManager created...")
        PeripheralManager.getInstance()
    }

    fun getInstance(): PeripheralManager = manager
}