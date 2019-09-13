package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface MainContract : BaseContract {
    interface View : BaseView<Presenter>
    interface Presenter : BasePresenter<View>
}