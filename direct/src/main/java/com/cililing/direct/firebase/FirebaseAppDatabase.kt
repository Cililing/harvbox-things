package com.cililing.direct.firebase

import com.cililing.harvbox.common.StatusSnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

internal interface FirebaseAppDatabase {
    fun post(model: StatusSnapshot)
}

internal class FirebaseAppDatabaseImpl(
        firebaseApp: FirebaseApp,
        private val firebaseFailureHandler: FirebaseFailureHandler
) : FirebaseAppDatabase {

    private val database by lazy {
        FirebaseDatabase.getInstance(firebaseApp)
    }

    private val realtimeStatus by lazy {
        database.reference.child("status")
    }

    override fun post(model: StatusSnapshot) {
        mapOf(
                "humidity" to model.humidityValue,
                "light1" to model.light1PowerOn,
                "light2" to model.light2PowerOn,
                "proximity" to model.proximityValue,
                "temp" to model.tempValue
        ).let {
            realtimeStatus.postForChild(it)
        }
    }

    private fun DatabaseReference.postForChild(
            valueMap: Map<String, Any?>
    ) {
        valueMap.forEach {
            child(it.key).setValue(it.value)
                    .addOnFailureListener { ex ->
                        firebaseFailureHandler.handle(ex)
                    }
        }
    }
}