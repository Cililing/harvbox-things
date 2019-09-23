package com.cililing.harvbox.common

import android.util.Log
import java.lang.Exception

interface Logger {
    fun i(what: String)
    fun e(what: String, why: String? = null, exception: Exception? = null)
}

class DebugLogger : Logger {

    companion object {
        private const val TAG = "ThingsControllerImpl"
    }

    override fun e(what: String, why: String?, exception: Exception?) {
        Log.e(TAG, "$what\n\t$why")
    }

    override fun i(what: String) {
        Log.d(TAG, what)
    }
}

class ReleaseLogger : Logger {
    override fun i(what: String) {
    }

    override fun e(what: String, why: String?, exception: Exception?) {
    }
}
