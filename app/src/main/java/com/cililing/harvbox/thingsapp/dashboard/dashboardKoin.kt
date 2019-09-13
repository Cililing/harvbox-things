package com.cililing.harvbox.thingsapp.dashboard

import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val dashboardScope = { module: Module ->
    module.scope(named<DashboardFragment>()) {
        scoped { params ->
            DashboardPresenter(
                    params[0]
            ) as DashboardContract.Presenter
        }
    }
}

fun DashboardContract.View.createPresenterParams(
        view: DashboardContract.View
): DefinitionParameters {
    return parametersOf(view)
}