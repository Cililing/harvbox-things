package com.cililing.harvbox.thingsapp.model

import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface AppFirebaseService {
    fun applyNewSettingsToLight1(lightTriggerSet: Set<LightTrigger>)
    fun applyNewSettingsToLight2(lightTriggerSet: Set<LightTrigger>)
}

class AppFirebaseServiceImpl(
    firebaseApp: FirebaseApp,
    light1CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    light2CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    private val appController: AppController,
    private val gson: Gson
) : AppFirebaseService {

    companion object {
        private val typeToken = object : TypeToken<List<LightTrigger>>() {}.type
    }

    private val firebaseDb = FirebaseDatabase.getInstance(firebaseApp)

    private val lightSettingsDbReference = firebaseDb.reference.child("light_status")
    private val light1SettingsReference = lightSettingsDbReference.child("light1")
    private val light2SettingsReference = lightSettingsDbReference.child("light2")

    init {
        lightSettingsDbReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val part1 = p0.child("light1").value?.toString()
                val part2 = p0.child("light2").value?.toString()

                val part1Result = gson.fromJson<List<LightTrigger>>(part1, typeToken)?.toSet() ?: setOf()
                val part2Result = gson.fromJson<List<LightTrigger>>(part2, typeToken)?.toSet() ?: setOf()

                appController.newLightSettingsReceived(part1Result, part2Result)
            }
        })

        light1SettingsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val json = dataSnapshot.value?.toString()
                val result = gson.fromJson<List<LightTrigger>>(json, typeToken)?.toSet() ?: return
                light1CurrentSnapshotProvider.newSnapshotAvailable(result)
            }
        })

        light2SettingsReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val json = dataSnapshot.value?.toString()
                val result = gson.fromJson<List<LightTrigger>>(json, typeToken)?.toSet() ?: return
                light2CurrentSnapshotProvider.newSnapshotAvailable(result)
            }
        })
    }

    override fun applyNewSettingsToLight1(lightTriggerSet: Set<LightTrigger>) {
        light1SettingsReference.setValue(lightTriggerSet.toList())
    }

    override fun applyNewSettingsToLight2(lightTriggerSet: Set<LightTrigger>) {
        light2SettingsReference.setValue(lightTriggerSet.toList())
    }
}