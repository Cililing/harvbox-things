package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.thingsapp.core.ProcuderScheduler
import com.cililing.harvbox.thingsapp.model.CurrentValuesProvider
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val dashboardScope = { module: Module ->
    module.scope(named<DashboardFragment>()) {
        scoped { params ->
            DashboardPresenter(
                    params[0], params[1], params[2]
            ) as DashboardContract.Presenter
        }
    }
}

fun DashboardContract.View.createPresenterParams(
        view: DashboardContract.View,
        producerScheduler: ProcuderScheduler,
        currentValuesProvider: CurrentValuesProvider
): DefinitionParameters {
    return parametersOf(view, producerScheduler, currentValuesProvider)
}