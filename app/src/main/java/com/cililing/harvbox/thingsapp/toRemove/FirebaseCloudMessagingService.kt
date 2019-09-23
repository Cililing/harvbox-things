package com.cililing.harvbox.thingsapp.toRemove

import android.util.Log
import com.cililing.harvbox.common.TAG
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "Token received: $token")
    }
}