package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl

class DashboardPresenter(
        view: DashboardContract.View
) : BasePresenterImpl<DashboardContract.View>(view), DashboardContract.Presenter {

}