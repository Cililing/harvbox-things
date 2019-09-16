package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface MainContract : BaseContract {
    interface View : BaseView<Presenter> {
        fun showTab(tab: Tab)
    }

    interface Presenter : BasePresenter<View> {
        val tabs: List<Tab>
        fun tabSelected(tab: Tab)
    }
}