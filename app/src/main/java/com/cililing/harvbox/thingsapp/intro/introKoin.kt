package com.cililing.harvbox.thingsapp.intro

import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val introScope = { module: Module ->
    module.scope(named<IntroFragment>()) {
        scoped { params ->
            IntroPresenter(
                    params[0]
            )
        }
    }
}

fun IntroFragment.getPresenterParams(
        view: IntroContract.View
): DefinitionParameters {
    return parametersOf(view)
}