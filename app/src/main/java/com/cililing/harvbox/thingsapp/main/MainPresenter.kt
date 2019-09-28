package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.ProducerScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainPresenter(
    view: MainContract.View,
    private val appController: AppController,
    private val currentSnapshotProvider: CurrentSnapshotProvider<StatusSnapshot>
) : BasePresenterImpl<MainContract.View>(view), MainContract.Presenter {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
            Dispatchers.Main + parentJob
    )

    private val scheduler: ProducerScheduler = ProducerScheduler(5000, null)

    override val tabs = listOf(Tab.Dashboard, Tab.Stats, Tab.Settings)

    override fun tabSelected(tab: Tab) {
        view.showTab(tab)
    }

    override fun onResume() {
        super.onResume()
        scheduler.start(::requestForData)
    }

    private fun requestForData() {
        coroutineScope.launch {
            val snapshot = appController.getData()
            currentSnapshotProvider.newSnapshotAvailable(snapshot)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }
}