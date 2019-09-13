package com.cililing.harvbox.thingsapp.settings

import org.koin.core.module.Module
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val settingsScope = { module: Module ->
    module.scope(named<SettingsFragment>()) {
        scoped { params ->
            SettingsPresenter(
                    params[0]
            ) as SettingsContract.Presenter
        }
    }
}

fun SettingsContract.View.getPresenterParams(
        view: SettingsContract.View
): DefinitionParameters {
    return parametersOf(view)
}