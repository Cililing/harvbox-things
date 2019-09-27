package com.cililing.direct.firebase

import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.StatusSnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

internal interface FirebaseAppDatabase {
    fun post(model: StatusSnapshot)
}

internal class FirebaseAppDatabaseImpl(
    firebaseApp: FirebaseApp,
    private val firebaseFailureHandler: FirebaseFailureHandler,
    private val firebaseNewDataCallback: FirebaseNewDataCallback,
    private val logger: Logger
) : FirebaseAppDatabase {

    private val database = FirebaseDatabase.getInstance(firebaseApp)
    private val realtimeStatus = database.reference.child("status")

    init {
        realtimeStatus.child("light1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                logger.i("light1 value received: $p0.value")
                (p0.value as? Boolean)?.let { firebaseNewDataCallback.onNewLight1(it) }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
        realtimeStatus.child("light2").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                logger.i("light2 value received: $p0.value")
                (p0.value as? Boolean)?.let { firebaseNewDataCallback.onNewLight2(it) }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun post(model: StatusSnapshot) {
        realtimeStatus.updateChildren(
                mapOf(
                        "timestamp" to model.timestamp,
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