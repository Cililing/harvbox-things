package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface DashboardContract : BaseContract {
    interface View: BaseView<Presenter>
    interface Presenter: BasePresenter<View>
}