package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val mainScope = { module: Module ->
    module.scope(named<MainFragment>()) {
        scoped { params ->
            MainPresenter(
                    params[0],
                    params[1],
                    params[2],
                params[3],
                params[4]
            ) as MainContract.Presenter
        }
    }
}

fun MainContract.View.getPresenterParams(
    view: MainContract.View,
    appController: AppController,
    currentSnapshotProvider: CurrentSnapshotProvider<StatusSnapshot>,
    realtimeDbCooldownProvider: CurrentSnapshotProvider<Long>,
    elasticCooldownProvider: CurrentSnapshotProvider<Long>
): DefinitionParameters {
    return parametersOf(view,
        appController,
        currentSnapshotProvider,
        realtimeDbCooldownProvider,
        elasticCooldownProvider
    )
}