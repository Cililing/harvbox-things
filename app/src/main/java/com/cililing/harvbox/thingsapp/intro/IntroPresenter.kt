package com.cililing.harvbox.thingsapp.intro

import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import com.cililing.harvbox.thingsapp.model.ConnectivityChecker
import kotlinx.coroutines.*

class IntroPresenter(
        view: IntroContract.View,
        private val connectivityChecker: ConnectivityChecker
) : BasePresenterImpl<IntroContract.View>(view), IntroContract.Presenter {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
    )

    override fun onResume() {
        super.onResume()
        checkNetwork()
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
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