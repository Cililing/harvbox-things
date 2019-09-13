package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl

class DashboardPresenter(
        override var view: DashboardContract.View
) : BasePresenterImpl<DashboardContract.View>(), DashboardContract.Presenter {

}