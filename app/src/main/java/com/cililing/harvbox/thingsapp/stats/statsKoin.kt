package com.cililing.harvbox.thingsapp.stats

import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val statsScope = { module: Module ->
    module.scope(named<StatsFragment>()) {
        scoped { params ->
            StatsPresenter(
                    params[0]
            ) as StatsContract.Presenter
        }
    }
}

fun StatsContract.View.getPresenterParams(
        view: StatsContract.View
): DefinitionParameters {
    return parametersOf(view)
}