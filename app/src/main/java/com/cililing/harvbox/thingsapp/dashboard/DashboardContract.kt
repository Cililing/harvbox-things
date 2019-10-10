package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface DashboardContract : BaseContract {
    interface View : BaseView<Presenter> {
        fun onNewTemperatureReceived(new: Float)
        fun onNewHumidityReceived(new: Float)
        fun onNewProximityReceived(new: Float)
        fun onNewSnapshotTimeReceived(new: String)
        fun onNewLight1StatusReceived(new: Boolean)
        fun onNewLight2StatusReceived(new: Boolean)
        fun onNewPhotoReceived(new: String?)
    }

    interface Presenter : BasePresenter<View> {
        fun onLight1Click(isOn: Boolean)
        fun onLight2Click(isOn: Boolean)
        fun onRequestPhotoClicked()
    }
}