package com.cililing.harvbox.thingsapp.core

import android.util.Log
import com.cililing.harvbox.thingsapp.common.TAG
import java.lang.Exception

interface AppLogger {
    fun i(source: Any?, what: String)
    fun e(source: Any?, what: String, exception: Exception)
}

class DebugLogger : AppLogger {
    override fun i(source: Any?, what: String) {
        Log.d(source.TAG, what)
    }

    override fun e(source: Any?, what: String, exception: Exception) {
        Log.d(source.TAG, "$what\n\t$exception")
    }
}

class ReleaseLogger : AppLogger {
    override fun i(source: Any?, what: String) {
    }

    override fun e(source: Any?, what: String, exception: Exception) {
    }
}