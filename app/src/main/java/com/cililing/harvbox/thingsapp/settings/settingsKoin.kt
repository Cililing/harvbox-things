package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.model.AppFirebaseService
import com.cililing.harvbox.thingsapp.model.LightTrigger
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val settingsScope = { module: Module ->
    module.scope(named<SettingsFragment>()) {
        scoped { params ->
            SettingsPresenter(
                    params[0],
                    params[1],
                    params[2],
                    params[3],
                    params[4]
            ) as SettingsContract.Presenter
        }
    }
}

fun SettingsContract.View.getPresenterParams(
    view: SettingsContract.View,
    appFirebaseService: AppFirebaseService,
    light1CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    light2CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    clock: Clock
): DefinitionParameters {
    return parametersOf(
            view,
            appFirebaseService,
            light1CurrentSnapshotProvider,
            light2CurrentSnapshotProvider,
            clock
    )
}