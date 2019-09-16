package com.cililing.harvbox.thingsapp.intro

import com.cililing.harvbox.thingsapp.model.ConnectivityChecker
import com.cililing.harvbox.thingsapp.model.ConnectivityCheckerImpl
import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val introScope = { module: Module ->
    module.scope(named<IntroFragment>()) {
        scoped { params ->
            IntroPresenter(
                    params[0], params[1]
            ) as IntroContract.Presenter
        }
    }
}

fun IntroContract.View.getPresenterParams(
        view: IntroContract.View,
        connectivityChecker: ConnectivityChecker
): DefinitionParameters {
    return parametersOf(view, connectivityChecker)
}