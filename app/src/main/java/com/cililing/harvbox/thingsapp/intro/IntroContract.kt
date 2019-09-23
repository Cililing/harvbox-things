package com.cililing.harvbox.thingsapp.intro

import com.cililing.harvbox.thingsapp.core.mvp.BaseContract
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenter
import com.cililing.harvbox.thingsapp.core.mvp.BaseView

interface IntroContract : BaseContract {
    interface View : BaseView<Presenter> {
        fun showProgress()
        fun showNetworkError()
        fun goToMainFragment()
    }

    interface Presenter : BasePresenter<View> {
        fun onRetryClick()
    }
}