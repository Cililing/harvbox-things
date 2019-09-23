package com.cililing.harvbox.thingsapp.core.mvp

interface BaseContract {
    interface View : BaseView<Presenter>
    interface Presenter : BasePresenter<View>
}