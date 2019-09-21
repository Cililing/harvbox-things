package com.cililing.harvbox.thingsapp._old

import android.util.Log
import com.cililing.harvbox.common.TAG
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

object FirebaseInstanceHelper {

    private val firebaseInstanceId: FirebaseInstanceId by lazy {
        FirebaseInstanceId.getInstance()
    }

    fun retreiveToken(listener: (String?) -> Unit) {
        firebaseInstanceId.instanceId
                .addOnCompleteListener( OnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", it.exception)
                        listener(null)
                        return@OnCompleteListener
                    }

                    val token = it.result?.token
                    Log.d(TAG, "getInstanceId finished with result: $token")
                    listener.invoke(token)
                })
    }

}