package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.ProducerScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import com.cililing.harvbox.thingsapp.model.CurrentValuesProvider
import kotlinx.coroutines.*

class DashboardPresenter(
        view: DashboardContract.View,
        private val appController: AppController,
        private val scheduler: ProducerScheduler,
        private val currentValuesProvider: CurrentValuesProvider
) : BasePresenterImpl<DashboardContract.View>(view), DashboardContract.Presenter {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
    )

    private var currentSnapshot: StatusSnapshot? = null

    override fun onResume() {
        super.onResume()

        // Schedule getting data
        scheduler.start(::requestForData)
    }

    override fun onDestroy() {
        super.onDestroy()
        scheduler.stop()
        parentJob.cancel()
    }

    private fun requestForData() {
        coroutineScope.launch {
            val firebaseSnapshot = async {
                appController.getData()
            }
            with(firebaseSnapshot.await()) {
                view.onNewSnapshot(this)
                currentSnapshot = this
            }
        }
    }

    override fun onLight1Click() {
        coroutineScope.launch {
            currentSnapshot?.let {
                appController.request(ThingsActionRequest.Light1(!it.light1PowerOn.isOn))
                requestForData()
            }
        }
    }

    override fun onLight2Click() {
        coroutineScope.launch {
            currentSnapshot?.let {
                appController.request(ThingsActionRequest.Light2(!it.light2PowerOn.isOn))
                requestForData()
            }
        }
    }

}