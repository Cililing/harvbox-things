package com.cililing.harvbox.thingsapp.main

import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val mainScope = { module: Module ->
    module.scope(named<MainFragment>()) {
        scoped { params ->
            MainPresenter(
                    params[0]
            ) as MainContract.Presenter
        }
    }
}

fun MainContract.View.getPresenterParams(
        view: MainContract.View
): DefinitionParameters {
    return parametersOf(view)
}