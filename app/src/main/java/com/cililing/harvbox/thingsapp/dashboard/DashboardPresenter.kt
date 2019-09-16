package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.thingsapp.core.ProcuderScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import com.cililing.harvbox.thingsapp.model.CurrentValuesProvider
import kotlinx.coroutines.*

class DashboardPresenter(
        view: DashboardContract.View,
        private val scheduler: ProcuderScheduler,
        private val currentValuesProvider: CurrentValuesProvider
) : BasePresenterImpl<DashboardContract.View>(view), DashboardContract.Presenter {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
    )

    override fun onResume() {
        super.onResume()
        scheduler.start(::requestForData)
    }

    override fun onDestroy() {
        super.onDestroy()
        scheduler.stop()
        parentJob.cancel()
    }

    private fun requestForData() {
        coroutineScope.launch {
            val light = async(Dispatchers.IO) { currentValuesProvider.requestLightStatus() }
            val temp = async(Dispatchers.IO) { currentValuesProvider.requestTemperature() }

            view.onNewTemperatureReceived(temp.await())
            view.onNewLightStatusReceived(light.await())
        }
    }

}