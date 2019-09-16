package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl

class MainPresenter(
        view: MainContract.View
) : BasePresenterImpl<MainContract.View>(view), MainContract.Presenter {

    override val tabs = listOf(Tab.Dashboard, Tab.Stats, Tab.Settings)

    override fun tabSelected(tab: Tab) {
        view.showTab(tab)
    }

}