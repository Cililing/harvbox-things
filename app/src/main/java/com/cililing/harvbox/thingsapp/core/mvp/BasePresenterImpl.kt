package com.cililing.harvbox.thingsapp.core.mvp

import androidx.annotation.CallSuper

abstract class BasePresenterImpl<View : BaseView<*>>(
    override var view: View
) : BasePresenter<View> {

    @CallSuper
    override fun onResume() {
    }

    @CallSuper
    override fun onPause() {
    }

    @CallSuper
    override fun onDestroy() {
    }
}