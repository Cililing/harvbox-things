package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView
import com.cililing.harvbox.thingsapp.model.LightStatus

interface DashboardContract : BaseContract {
    interface View : BaseView<Presenter> {
        fun onNewTemperatureReceived(new: Float)
        fun onNewLightStatusReceived(new: LightStatus)
        fun onNewSnapshot(new: StatusSnapshot)
    }

    interface Presenter : BasePresenter<View> {
        fun onLight1Click()
        fun onLight2Click()
    }
}