package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface SettingsContract : BaseContract {
    interface View : BaseView<Presenter>
    interface Presenter : BasePresenter<View>
}