package com.cililing.harvbox.thingsapp.core.mvp

abstract class BasePresenterImpl<View: BaseView<*>> (
        override var view: View
): BasePresenter<View>