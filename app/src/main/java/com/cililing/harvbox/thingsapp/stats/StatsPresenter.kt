package com.cililing.harvbox.thingsapp.stats

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl

class StatsPresenter(
        view: StatsContract.View
) : BasePresenterImpl<StatsContract.View>(view), StatsContract.Presenter