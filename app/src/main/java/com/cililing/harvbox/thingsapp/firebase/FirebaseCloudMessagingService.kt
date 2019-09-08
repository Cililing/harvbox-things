package com.cililing.harvbox.thingsapp.firebase

import android.util.Log
import com.cililing.harvbox.thingsapp.common.TAG
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "Token received: $token")
    }

}