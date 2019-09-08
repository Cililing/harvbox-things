package com.cililing.harvbox.thingsapp.thingscontroller.core

import android.util.Log
import java.lang.Exception

internal interface Logger {
    fun i(what: String)
    fun e(what: String, why: String? = null, exception: Exception? = null)
}

internal class DebugLogger: Logger {

    companion object {
        private const val TAG = "ThingsController"
    }

    override fun e(what: String, why: String?, exception: Exception?) {
        Log.e(TAG, "$what\n\t$why")
    }

    override fun i(what: String) {
        Log.d(TAG, what)
    }

}

internal class ReleaseLogger: Logger {
    override fun i(what: String) {
    }

    override fun e(what: String, why: String?, exception: Exception?) {
    }
}
