package com.cililing.direct.firebase

import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.toJson
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

internal interface FirebaseAppDatabase {
    fun post(model: StatusSnapshot)
}

internal class FirebaseAppDatabaseImpl(
        firebaseApp: FirebaseApp,
        private val firebaseFailureHandler: FirebaseFailureHandler,
        private val logger: Logger
) : FirebaseAppDatabase {

    private val database by lazy {
        FirebaseDatabase.getInstance(firebaseApp)
    }

    private val realtimeStatus by lazy {
        database.reference.child("status")
    }

    override fun post(model: StatusSnapshot) {
        realtimeStatus.updateChildren(
                mapOf(
                        "humidity" to model.humidityValue,
                        "light1" to model.light1PowerOn,
                        "light2" to model.light2PowerOn,
                        "proximity" to model.proximityValue,
                        "temp" to model.tempValue)
        ).addOnFailureListener {
            firebaseFailureHandler.handle(it)
        }
    }
}