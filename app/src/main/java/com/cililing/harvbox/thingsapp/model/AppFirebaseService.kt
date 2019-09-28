package com.cililing.harvbox.thingsapp.model

import com.cililing.harvbox.common.LightTimerSnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

interface AppFirebaseService {
    fun applyNewSettingsToLight1(lightTimerSnapshot: LightTimerSnapshot)
    fun applyNewSettingsToLight2(lightTimerSnapshot: LightTimerSnapshot)
}

class AppFirebaseServiceImpl(
        firebaseApp: FirebaseApp
) : AppFirebaseService {

    private val firebaseDb = FirebaseDatabase.getInstance(firebaseApp)

    private val lightSettingsDbReference = firebaseDb.reference.child("light_status")
    private val light1SettingsReference = lightSettingsDbReference.child("light1")
    private val light2SettingsReference = lightSettingsDbReference.child("light2")

    override fun applyNewSettingsToLight1(lightTimerSnapshot: LightTimerSnapshot) = updateLight(
            light1SettingsReference,
            lightTimerSnapshot
    )

    override fun applyNewSettingsToLight2(lightTimerSnapshot: LightTimerSnapshot) = updateLight(
            light2SettingsReference,
            lightTimerSnapshot
    )

    private fun updateLight(lightReference: DatabaseReference, lightTimerSnapshot: LightTimerSnapshot) {
        lightReference.updateChildren(mapOf(
                "turn_on" to lightTimerSnapshot.turnOn,
                "turn_off" to lightTimerSnapshot.turnOff)
        )
    }
}