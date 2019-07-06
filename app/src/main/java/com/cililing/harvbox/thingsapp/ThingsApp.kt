package com.cililing.harvbox.thingsapp

import android.app.Application
import android.util.Log
import com.cililing.harvbox.thingsapp.common.TAG
import com.cililing.harvbox.thingsapp.firebase.FirebaseInstanceHelper

class ThingsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Obtain FirebaseId:
        FirebaseInstanceHelper.retreiveToken {
            Log.d(TAG, "Obtained token: $it")
        }
    }

}