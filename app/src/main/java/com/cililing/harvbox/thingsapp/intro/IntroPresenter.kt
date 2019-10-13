package com.cililing.harvbox.thingsapp.intro

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import com.cililing.harvbox.thingsapp.core.ConnectivityChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IntroPresenter(
    view: IntroContract.View,
    private val connectivityChecker: ConnectivityChecker
) : BasePresenterImpl<IntroContract.View>(view), IntroContract.Presenter {

    override fun onResume() {
        super.onResume()
        checkNetwork()
    }

    override fun onRetryClick() {
        checkNetwork()
    }

    private fun checkNetwork() {
        view.showProgress()
        coroutineScope.launch {
            val isNetworkOk = withContext(Dispatchers.IO) {
                connectivityChecker.isNetworkOk()
            }

            if (isNetworkOk) {
                view.goToMainFragment()
            } else {
                view.showNetworkError()
            }
        }
    }
}