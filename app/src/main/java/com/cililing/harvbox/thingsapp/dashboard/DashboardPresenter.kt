package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import com.cililing.harvbox.thingsapp.core.LightStatusHandler
import com.cililing.harvbox.thingsapp.settings.SettingsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardPresenter(
    view: DashboardContract.View,
    private val appController: AppController,
    private val currentSnapshotProvider: CurrentSnapshotProvider<StatusSnapshot>,
    private val lastPhotoProvider: CurrentSnapshotProvider<String>,
    private val lightStatusHandler: LightStatusHandler
) : BasePresenterImpl<DashboardContract.View>(view), DashboardContract.Presenter {

    override fun onResume() {
        currentSnapshotProvider.registerListener(currentSnapshotListener)
        lastPhotoProvider.registerListener(lastPhotoListener)
        super.onResume()
    }

    override fun onPause() {
        currentSnapshotProvider.unregisterListener(currentSnapshotListener)
        lastPhotoProvider.unregisterListener(lastPhotoListener)
        super.onPause()
    }

    private val currentSnapshotListener: CurrentSnapshotProvider.Listener<StatusSnapshot> =
        object : CurrentSnapshotProvider.Listener<StatusSnapshot> {
            override fun onNewSnapshot(snapshot: StatusSnapshot) {
                view.run {
                    onNewSnapshotTimeReceived(snapshot.timestamp ?: "null")
                    onNewProximityReceived(snapshot.proximityValue.toFloat())
                    onNewHumidityReceived(snapshot.humidityValue.toFloat())
                    onNewTemperatureReceived(snapshot.tempValue.toFloat())
                    onNewLight1StatusReceived(
                        snapshot.light1PowerOn,
                        lightStatusHandler.isInRequiredState(
                            SettingsContract.LightId.LIGHT_1,
                            snapshot.light1PowerOn
                        ) ?: false
                    )
                    onNewLight2StatusReceived(
                        snapshot.light2PowerOn,
                        lightStatusHandler.isInRequiredState(
                            SettingsContract.LightId.LIGHT_2,
                            snapshot.light2PowerOn
                        ) ?: false
                    )
                }
            }
        }

    private val lastPhotoListener: CurrentSnapshotProvider.Listener<String> =
        object : CurrentSnapshotProvider.Listener<String> {
            override fun onNewSnapshot(snapshot: String) {
                view.onNewPhotoReceived(snapshot)
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
            appController.request(ThingsActionRequest.Light1(isOn))
            requestForData()
        }
    }

    override fun onLight2Click(isOn: Boolean) {
        coroutineScope.launch {
            appController.request(ThingsActionRequest.Light2(isOn))
            requestForData()
        }
    }

    override fun onRequestPhotoClicked() {
        coroutineScope.launch {
            view.onNewPhotoReceived(null)
        }
        appController.requestPhoto()
    }
}