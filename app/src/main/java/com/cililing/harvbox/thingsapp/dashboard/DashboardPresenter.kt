package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardPresenter(
    view: DashboardContract.View,
    private val appController: AppController,
    private val currentSnapshotProvider: CurrentSnapshotProvider<StatusSnapshot>
) : BasePresenterImpl<DashboardContract.View>(view), DashboardContract.Presenter {

    override fun onResume() {
        currentSnapshotProvider.registerListener(currentSnapshotListener)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        currentSnapshotProvider.unregisterListener(currentSnapshotListener)
    }

    private val currentSnapshotListener: CurrentSnapshotProvider.Listener<StatusSnapshot> =
            object : CurrentSnapshotProvider.Listener<StatusSnapshot> {
                override fun onNewSnapshot(snapshot: StatusSnapshot) {
                    view.run {
                        onNewSnapshotTimeReceived(snapshot.timestamp ?: "null")
                        onNewProximityReceived(snapshot.proximityValue.toFloat())
                        onNewHumidityReceived(snapshot.humidityValue.toFloat())
                        onNewTemperatureReceived(snapshot.tempValue.toFloat())
                        onNewLight1StatusReceived(snapshot.light1PowerOn)
                        onNewLight2StatusReceived(snapshot.light2PowerOn)
                    }
                }
            }

    private fun requestForData() {
        coroutineScope.launch {
            val firebaseSnapshot = appController.getData()
            currentSnapshotProvider.newSnapshotAvailable(firebaseSnapshot)
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