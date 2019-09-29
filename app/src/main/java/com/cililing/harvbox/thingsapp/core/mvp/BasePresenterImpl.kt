package com.cililing.harvbox.thingsapp.core.mvp

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BasePresenterImpl<View : BaseView<*>>(
    override var view: View
) : BasePresenter<View> {

    protected val parentJob = Job()
    protected open val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
    )

    @CallSuper
    override fun onResume() {
    }

    @CallSuper
    override fun onPause() {
    }

    @CallSuper
    override fun onDestroy() {
        parentJob.cancel()
    }
}