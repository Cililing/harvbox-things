package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.LightStatusHandler
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val dashboardScope = { module: Module ->
    module.scope(named<DashboardFragment>()) {
        scoped { params ->
            DashboardPresenter(
                    params[0], params[1], params[2], params[3], params[4]
            ) as DashboardContract.Presenter
        }
    }
}

fun DashboardContract.View.createPresenterParams(
    view: DashboardContract.View,
    appController: AppController,
    currentSnapshotProvider: CurrentSnapshotProvider<StatusSnapshot>,
    lastPhotoProvider: CurrentSnapshotProvider<String>,
    lightStatusHandler: LightStatusHandler
): DefinitionParameters {
    return parametersOf(
        view,
        appController,
        currentSnapshotProvider,
        lastPhotoProvider,
        lightStatusHandler
    )
}