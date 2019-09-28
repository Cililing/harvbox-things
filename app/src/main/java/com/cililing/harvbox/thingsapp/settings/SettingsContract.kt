package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface SettingsContract : BaseContract {
    interface MenuView {
        fun showAppSettings()
        fun showLightSettings()
        fun showAboutApp()
    }

    interface AboutAppView

    interface AppSettingsView {

    }

    interface View : BaseView<Presenter>, MenuView, AboutAppView, AppSettingsView

    interface MenuPresenter {
        fun onAppSettingsClicked()
        fun onLightSettingsClicked()
        fun onShowAboutAppClicked()
    }

    interface AboutAppPresenter

    interface AppSettingsPresenter {

    }

    interface Presenter : BasePresenter<View>, MenuPresenter, AboutAppPresenter, AppSettingsPresenter
}