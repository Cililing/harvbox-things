package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import com.cililing.harvbox.thingsapp.model.AppFirebaseService
import com.cililing.harvbox.thingsapp.model.LightTrigger
import com.cililing.harvbox.thingsapp.model.TriggerType

class SettingsPresenter(
    view: SettingsContract.View,
    private val appFirebaseService: AppFirebaseService,
    private val light1CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    private val light2CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    private val realtimeDbCurrentSnapshotProvider: CurrentSnapshotProvider<Long>,
    private val elasticCurrentSnapshotProvider: CurrentSnapshotProvider<Long>,
    private val photoCooldownSnapshotProvider: CurrentSnapshotProvider<Long>,
    private val clock: Clock
) : BasePresenterImpl<SettingsContract.View>(view), SettingsContract.Presenter {

    private var light1Triggers = mutableSetOf<LightTrigger>()
    private var light2Triggers = mutableSetOf<LightTrigger>()

    private val light1CurrentSnapshotListener =
            object : CurrentSnapshotProvider.Listener<Set<LightTrigger>> {
                override fun onNewSnapshot(snapshot: Set<LightTrigger>) {
                    light1Triggers = snapshot.toMutableSet()
                    view.fillTriggers(SettingsContract.LightId.LIGHT_1, light1Triggers)
                }
            }

    private val light2CurrentSnapshotListener =
            object : CurrentSnapshotProvider.Listener<Set<LightTrigger>> {
                override fun onNewSnapshot(snapshot: Set<LightTrigger>) {
                    light2Triggers = snapshot.toMutableSet()
                    view.fillTriggers(SettingsContract.LightId.LIGHT_2, light2Triggers)
                }
            }

    private val realtimeDbCurrentSnapshotListener =
        object : CurrentSnapshotProvider.Listener<Long> {
            override fun onNewSnapshot(snapshot: Long) {
                view.setRealtimeCooldown(snapshot)
            }
        }

    private val elasticCurrentSnapshotListener =
        object : CurrentSnapshotProvider.Listener<Long> {
            override fun onNewSnapshot(snapshot: Long) {
                view.setElasticCooldown(snapshot)
            }
        }

    private val photoCooldownCurrentSnapshotListener =
        object : CurrentSnapshotProvider.Listener<Long> {
            override fun onNewSnapshot(snapshot: Long) {
                view.setPhotoCooldown(snapshot)
            }
        }

    override fun onResume() {
        super.onResume()

        view.fillTriggers(SettingsContract.LightId.LIGHT_1, light1Triggers)
        view.fillTriggers(SettingsContract.LightId.LIGHT_2, light2Triggers)

        light1CurrentSnapshotProvider.registerListener(light1CurrentSnapshotListener)
        light2CurrentSnapshotProvider.registerListener(light2CurrentSnapshotListener)
        elasticCurrentSnapshotProvider.registerListener(elasticCurrentSnapshotListener)
        realtimeDbCurrentSnapshotProvider.registerListener(realtimeDbCurrentSnapshotListener)
        photoCooldownSnapshotProvider.registerListener(photoCooldownCurrentSnapshotListener)
    }

    override fun onPause() {
        super.onPause()

        light1CurrentSnapshotProvider.unregisterListener(light1CurrentSnapshotListener)
        light2CurrentSnapshotProvider.unregisterListener(light2CurrentSnapshotListener)
        elasticCurrentSnapshotProvider.unregisterListener(elasticCurrentSnapshotListener)
        realtimeDbCurrentSnapshotProvider.unregisterListener(realtimeDbCurrentSnapshotListener)
        photoCooldownSnapshotProvider.unregisterListener(photoCooldownCurrentSnapshotListener)
    }

    override fun onAppSettingsClicked() = view.showAppSettings()
    override fun onLightSettingsClicked() = view.showLightSettings()
    override fun onShowAboutAppClicked() = view.showAboutApp()

    override fun onNewLightTriggerClicked(lightId: SettingsContract.LightId) {
        view.showTimePicker(lightId, clock.currentHour(), clock.currentMin())
    }

    override fun onNewTriggerSelected(lightId: SettingsContract.LightId, hour: Int, minute: Int) {
        updateTriggers(lightId, getTriggerSet(lightId), LightTrigger(hour, minute, TriggerType.ON))
    }

    override fun changeTriggerType(
        lightId: SettingsContract.LightId,
        lightTrigger: LightTrigger,
        isOn: Boolean
    ) {
        val trigger = getTriggerSet(lightId).find { it == lightTrigger } ?: return
        trigger.type = if (isOn) TriggerType.ON else TriggerType.OFF
        updateAndFillTriggers(lightId)
    }

    override fun removeTrigger(lightId: SettingsContract.LightId, lightTrigger: LightTrigger) {
        getTriggerSet(lightId).remove(lightTrigger)
        updateAndFillTriggers(lightId)
    }

    private fun getTriggerSet(lightId: SettingsContract.LightId): MutableSet<LightTrigger> {
        return when (lightId) {
            SettingsContract.LightId.LIGHT_1 -> light1Triggers
            SettingsContract.LightId.LIGHT_2 -> light2Triggers
        }
    }

    private fun updateTriggers(
        lightId: SettingsContract.LightId,
        triggerSet: MutableSet<LightTrigger>,
        lightTrigger: LightTrigger
    ) {
        // Check if light trigger exists.
        val trigger = triggerSet.find { it == lightTrigger }

        if (trigger != null) {
            trigger.type = lightTrigger.type
        } else {
            triggerSet.add(lightTrigger)
        }

        updateAndFillTriggers(lightId)
    }

    private fun updateAndFillTriggers(lightId: SettingsContract.LightId) {
        val (reference, triggers) = when (lightId) {
            SettingsContract.LightId.LIGHT_1 -> Pair(appFirebaseService::applyNewSettingsToLight1, light1Triggers)
            SettingsContract.LightId.LIGHT_2 -> Pair(appFirebaseService::applyNewSettingsToLight2, light2Triggers)
        }

        view.fillTriggers(lightId, triggers)

        reference(triggers)
    }

    override fun onElasticCooldownOkClicked(value: Long) {
        appFirebaseService.setNewElasticCooldown(value)
    }

    override fun onRealtimeDbCooldownOkClicked(value: Long) {
        appFirebaseService.setNewRelatimeDbCooldown(value)
    }

    override fun onPhotoCooldownOkClicked(value: Long) {
        appFirebaseService.setNewPhotoCooldown(value)
    }
}