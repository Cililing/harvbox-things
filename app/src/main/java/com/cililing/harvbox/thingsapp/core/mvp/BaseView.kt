package com.cililing.harvbox.thingsapp.core.mvp

interface BaseView<out Presenter : BasePresenter<*>> {
    val presenter: Presenter
}