package com.cililing.direct.firebase

import android.net.Uri
import com.cililing.harvbox.common.FirebaseConstans
import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.StatusSnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

internal interface FirebaseRealtimeDatabase {
    fun post(model: StatusSnapshot)
    fun newPhotoAvailable(it: Uri)
}

internal class FirebaseRealtimeDatabaseImpl(
    firebaseApp: FirebaseApp,
    private val firebaseFailureHandler: FirebaseFailureHandler,
    private val firebaseNewDataCallback: FirebaseNewDataCallback,
    private val logger: Logger
) : FirebaseRealtimeDatabase {

    private val database = FirebaseDatabase.getInstance(firebaseApp)
    private val realtimeStatus = database.reference.child(FirebaseConstans.Realtime.Status.name)
    private val lastPhoto = database.reference.child(FirebaseConstans.Realtime.lastPhoto)

    private val triggers = database.reference.child(FirebaseConstans.Realtime.Triggers.name)
    private val photoTrigger = triggers.child(FirebaseConstans.Realtime.Triggers.photo)

    init {
        realtimeStatus.child(FirebaseConstans.Realtime.Status.light1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                logger.i("light1 value received: $p0.value")
                (p0.value as? Boolean)?.let { firebaseNewDataCallback.onNewLight1(it) }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
        realtimeStatus.child(FirebaseConstans.Realtime.Status.light2).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                logger.i("light2 value received: $p0.value")
                (p0.value as? Boolean)?.let { firebaseNewDataCallback.onNewLight2(it) }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        photoTrigger.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value == true) {
                    logger.i("Triggering photo")
                    firebaseNewDataCallback.onPhotoRequested()
                }
            }
        })
    }

    override fun post(model: StatusSnapshot) {
        realtimeStatus.updateChildren(
            mapOf(
                FirebaseConstans.Realtime.Status.timestamp to model.timestamp,
                FirebaseConstans.Realtime.Status.humidity to model.humidityValue,
                FirebaseConstans.Realtime.Status.light1 to model.light1PowerOn,
                FirebaseConstans.Realtime.Status.light2 to model.light2PowerOn,
                FirebaseConstans.Realtime.Status.proximity to model.proximityValue,
                FirebaseConstans.Realtime.Status.temp to model.tempValue)
        ).addOnFailureListener {
            firebaseFailureHandler.handle(it)
        }
    }

    override fun newPhotoAvailable(it: Uri) {
        lastPhoto.setValue(it.toString())
    }
}