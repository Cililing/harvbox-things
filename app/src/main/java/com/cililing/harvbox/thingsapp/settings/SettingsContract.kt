package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView
import com.cililing.harvbox.thingsapp.model.LightTrigger

interface SettingsContract : BaseContract {
    enum class LightId {
        LIGHT_1, LIGHT_2
    }

    interface MenuView {
        fun showAppSettings()
        fun showLightSettings()
        fun showAboutApp()
    }

    interface AboutAppView

    interface AppSettingsView {
        fun setElasticCooldown(value: Long)
        fun setRealtimeCooldown(value: Long)
        fun setPhotoCooldown(value: Long)
    }

    interface LightSettingsView {
        fun fillTriggers(lightId: LightId, triggerSet: Set<LightTrigger>)
        fun showTimePicker(
            lightId: LightId,
            hour: Int,
            minute: Int
        )
    }

    interface View : BaseView<Presenter>, MenuView, AboutAppView, AppSettingsView, LightSettingsView

    interface MenuPresenter {
        fun onAppSettingsClicked()
        fun onLightSettingsClicked()
        fun onShowAboutAppClicked()
    }

    interface AboutAppPresenter

    interface AppSettingsPresenter {
        fun onElasticCooldownOkClicked(value: Long)
        fun onRealtimeDbCooldownOkClicked(value: Long)
        fun onPhotoCooldownOkClicked(value: Long)
    }

    interface LightSettingsPresenter {
        fun onNewLightTriggerClicked(lightId: LightId)
        fun onNewTriggerSelected(lightId: LightId, hour: Int, minute: Int)
        fun changeTriggerType(lightId: LightId, lightTrigger: LightTrigger, isOn: Boolean)
        fun removeTrigger(lightId: LightId, lightTrigger: LightTrigger)
    }

    interface Presenter : BasePresenter<View>, MenuPresenter, AboutAppPresenter,
            AppSettingsPresenter, LightSettingsPresenter
}