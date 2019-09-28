package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl

class SettingsPresenter(
        view: SettingsContract.View
) : BasePresenterImpl<SettingsContract.View>(view), SettingsContract.Presenter {

    override fun onAppSettingsClicked() = view.showAppSettings()
    override fun onLightSettingsClicked() = view.showLightSettings()
    override fun onShowAboutAppClicked() = view.showAboutApp()


}