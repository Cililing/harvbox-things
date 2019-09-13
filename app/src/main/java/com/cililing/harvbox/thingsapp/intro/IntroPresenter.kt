package com.cililing.harvbox.thingsapp.intro

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl

class IntroPresenter(
        view: IntroContract.View
) : BasePresenterImpl<IntroContract.View>(view), IntroContract.Presenter