package com.cililing.harvbox.thingsapp.model

import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.settings.SettingsContract

interface LightStatusHandler {
    fun isInRequiredState(lightId: SettingsContract.LightId, currentStatus: Boolean): Boolean?
}

class LightStatusHandlerImpl(
    private val clock: Clock,
    light1Provider: CurrentSnapshotProvider<Set<LightTrigger>>,
    light2Provider: CurrentSnapshotProvider<Set<LightTrigger>>
) : LightStatusHandler {

    private var light1Triggers: Set<LightTrigger>? = null
    private var light2Triggers: Set<LightTrigger>? = null

    init {
        light1Provider.registerListener(object : CurrentSnapshotProvider.Listener<Set<LightTrigger>> {
            override fun onNewSnapshot(snapshot: Set<LightTrigger>) {
                light1Triggers = snapshot
            }
        })
        light2Provider.registerListener(object : CurrentSnapshotProvider.Listener<Set<LightTrigger>> {
            override fun onNewSnapshot(snapshot: Set<LightTrigger>) {
                light2Triggers = snapshot
            }
        })
    }

    override fun isInRequiredState(lightId: SettingsContract.LightId, currentStatus: Boolean): Boolean? {
        return when (lightId) {
            SettingsContract.LightId.LIGHT_1 -> checkLight(light1Triggers, currentStatus)
            SettingsContract.LightId.LIGHT_2 -> checkLight(light2Triggers, currentStatus)
        }
    }

    private fun checkLight(lightSet: Set<LightTrigger>?, currentStatus: Boolean): Boolean? {
        if (lightSet.isNullOrEmpty()) return null

        val currentHour = clock.currentHour()
        val currentMin = clock.currentMin()

        val sortedSet = lightSet.sortedWith(compareBy({ it.hour }, { it.minute }))

        val shouldBe = sortedSet.findLast {
            it.hour <= currentHour && it.minute < currentMin
        } ?: sortedSet[0]

        return (shouldBe.type == TriggerType.ON && currentStatus) || (shouldBe.type == TriggerType.OFF && !currentStatus)
    }

}