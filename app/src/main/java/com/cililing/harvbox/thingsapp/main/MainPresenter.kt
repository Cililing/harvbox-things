package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.ProducerScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BasePresenterImpl
import kotlinx.coroutines.launch

class MainPresenter(
    view: MainContract.View,
    private val appController: AppController,
    private val currentSnapshotProvider: CurrentSnapshotProvider<StatusSnapshot>,
    private val realtimeDbCooldownProvider: CurrentSnapshotProvider<Long>
) : BasePresenterImpl<MainContract.View>(view), MainContract.Presenter {

    private var scheduler: ProducerScheduler? = null

    override val tabs = listOf(Tab.Dashboard, Tab.Stats, Tab.Settings)

    override fun tabSelected(tab: Tab) {
        view.showTab(tab)
    }

    override fun onResume() {
        super.onResume()
        scheduler?.start(::requestForData)
        realtimeDbCooldownProvider.registerListener(object : CurrentSnapshotProvider.Listener<Long> {
            override fun onNewSnapshot(snapshot: Long) {
                scheduler?.stop()
                scheduler = ProducerScheduler(snapshot, null)
                scheduler?.start(::requestForData)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        scheduler?.stop()
    }

    private fun requestForData() {
        coroutineScope.launch {
            val snapshot = appController.getData()
            currentSnapshotProvider.newSnapshotAvailable(snapshot)
        }
    }
}