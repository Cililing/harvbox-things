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

    fun setNewElasticCooldown(cooldown: Long)
    fun setNewRelatimeDbCooldown(cooldown: Long)
    fun setNewPhotoCooldown(cooldown: Long)
}

class AppFirebaseServiceImpl(
    firebaseApp: FirebaseApp,
    light1CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    light2CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    elasticCooldownSnapshotProvider: CurrentSnapshotProvider<Long>,
    realtimeDbCooldownSnapshotProvider: CurrentSnapshotProvider<Long>,
    lastPhotoSnapshotProvider: CurrentSnapshotProvider<String>,
    photoCooldownSnapshotProvider: CurrentSnapshotProvider<Long>,
    private val appController: AppController,
    private val gson: Gson
) : AppFirebaseService {

    companion object {
        private val typeToken = object : TypeToken<List<LightTrigger>>() {}.type

        private const val REALTIME_DB_COOLDOWN = 5_000L // every 5 sec
        private const val ELASTIC_COOLDOWN = 1_000L * 60L * 5L // every 5 min
        private const val PHOTO_COOLDOWN = 1_000L * 60L * 5L
    }

    private val firebaseDb = FirebaseDatabase.getInstance(firebaseApp)

    private val lightSettingsDbReference = firebaseDb.reference.child("light_status")
    private val light1SettingsReference = lightSettingsDbReference.child("light1")
    private val light2SettingsReference = lightSettingsDbReference.child("light2")

    private val appSettingsDbReference = firebaseDb.reference.child("app_settings")
    private val appSettingsElasticCooldown = appSettingsDbReference.child("elastic_cooldown")
    private val appSettingsRealtimeCooldown = appSettingsDbReference.child("realtime_db_cooldown")
    private val appSettingsPhotoCooldown= appSettingsDbReference.child("photo_cooldown")

    private val lastPhotoReference = firebaseDb.reference.child("last_photo")

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

        light1SettingsReference.addValueEventListener(ProvideValueEventListener(
            light1CurrentSnapshotProvider
        ) {
            val json = it.value?.toString()
            gson.fromJson<List<LightTrigger>>(json, typeToken)?.toSet()
        })

        light2SettingsReference.addValueEventListener(ProvideValueEventListener(
            light2CurrentSnapshotProvider
        ) {
            val json = it.value?.toString()
            gson.fromJson<List<LightTrigger>>(json, typeToken)?.toSet()
        })

        appSettingsElasticCooldown.addValueEventListener(ProvideValueEventListener(
            elasticCooldownSnapshotProvider
        ) {
            it.value?.toString()?.toLongOrNull() ?: ELASTIC_COOLDOWN
        })

        appSettingsRealtimeCooldown.addValueEventListener(ProvideValueEventListener(
            realtimeDbCooldownSnapshotProvider
        ) {
            it.value?.toString()?.toLongOrNull() ?: REALTIME_DB_COOLDOWN
        })

        lastPhotoReference.addValueEventListener(ProvideValueEventListener(
            lastPhotoSnapshotProvider
        ) {
            it.value?.toString() ?: ""
        })

        appSettingsPhotoCooldown.addValueEventListener(ProvideValueEventListener(
            photoCooldownSnapshotProvider
        ) {
            it.value?.toString()?.toLongOrNull() ?: PHOTO_COOLDOWN
        })
    }

    override fun applyNewSettingsToLight1(lightTriggerSet: Set<LightTrigger>) {
        light1SettingsReference.setValue(lightTriggerSet.toList())
    }

    override fun applyNewSettingsToLight2(lightTriggerSet: Set<LightTrigger>) {
        light2SettingsReference.setValue(lightTriggerSet.toList())
    }

    override fun setNewElasticCooldown(cooldown: Long) {
        appSettingsElasticCooldown.setValue(cooldown)
    }

    override fun setNewRelatimeDbCooldown(cooldown: Long) {
        appSettingsRealtimeCooldown.setValue(cooldown)
    }

    override fun setNewPhotoCooldown(cooldown: Long) {
        appSettingsPhotoCooldown.setValue(cooldown)
    }
}