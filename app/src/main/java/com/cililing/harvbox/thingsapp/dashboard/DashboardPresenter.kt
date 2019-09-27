package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.ProducerScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardPresenter(
    view: DashboardContract.View,
    private val appController: AppController,
    private val scheduler: ProducerScheduler
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
                currentSnapshot = this
                view.onNewSnapshotTimeReceived(currentSnapshot?.timestamp ?: "null")
                view.onNewProximityReceived(currentSnapshot?.proximityValue?.toFloat() ?: 0.0f)
                view.onNewHumidityReceived(currentSnapshot?.humidityValue?.toFloat() ?: 0.0f)
                view.onNewTemperatureReceived(currentSnapshot?.tempValue?.toFloat() ?: 0.0f)
                view.onNewLight1StatusReceived(currentSnapshot?.light1PowerOn ?: false)
                view.onNewLight2StatusReceived(currentSnapshot?.light2PowerOn ?: false)
            }
        }
    }

    override fun onLight1Click(isOn: Boolean) {
        coroutineScope.launch {
            withContext(Dispatchers.Default) { appController.request(ThingsActionRequest.Light1(isOn)) }
            requestForData()
        }
    }

    override fun onLight2Click(isOn: Boolean) {
        coroutineScope.launch {
            withContext(Dispatchers.Default) { appController.request(ThingsActionRequest.Light2(isOn)) }
            requestForData()
        }
    }
}