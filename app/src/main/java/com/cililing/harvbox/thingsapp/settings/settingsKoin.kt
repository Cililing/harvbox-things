package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.FirebaseRealtimeDatabase
import com.cililing.harvbox.thingsapp.core.LightTrigger
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val settingsScope = { module: Module ->
    module.scope(named<SettingsFragment>()) {
        scoped { defParams ->
            val params: List<Any> = defParams[0]
            @Suppress("UNCHECKED_CAST")
            SettingsPresenter(
                params[0] as SettingsContract.View,
                params[1] as FirebaseRealtimeDatabase,
                params[2] as CurrentSnapshotProvider<Set<LightTrigger>>,
                params[3] as CurrentSnapshotProvider<Set<LightTrigger>>,
                params[4] as CurrentSnapshotProvider<Long>,
                params[5] as CurrentSnapshotProvider<Long>,
                params[6] as CurrentSnapshotProvider<Long>,
                params[7] as Clock
            ) as SettingsContract.Presenter
        }
    }
}

fun SettingsContract.View.getPresenterParams(
    view: SettingsContract.View,
    firebaseRealtimeDatabase: FirebaseRealtimeDatabase,
    light1CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    light2CurrentSnapshotProvider: CurrentSnapshotProvider<Set<LightTrigger>>,
    realtimeDbCurrentSnapshotProvider: CurrentSnapshotProvider<Long>,
    elasticCurrentSnapshotProvider: CurrentSnapshotProvider<Long>,
    photoCooldownSnapshotProvider: CurrentSnapshotProvider<Long>,
    clock: Clock
): DefinitionParameters {
    return parametersOf(
        listOf(
            view,
            firebaseRealtimeDatabase,
            light1CurrentSnapshotProvider,
            light2CurrentSnapshotProvider,
            realtimeDbCurrentSnapshotProvider,
            elasticCurrentSnapshotProvider,
            photoCooldownSnapshotProvider,
            clock
        )
    )
}