package com.cililing.harvbox.thingsapp.stats

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface StatsContract : BaseContract {
    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter<View>
}