package com.cililing.harvbox.thingsapp.core

import com.cililing.harvbox.thingsapp.dashboard.dashboardScope
import com.cililing.harvbox.thingsapp.intro.introScope
import com.cililing.harvbox.thingsapp.main.mainScope
import com.cililing.harvbox.thingsapp.model.ConnectivityChecker
import com.cililing.harvbox.thingsapp.model.ConnectivityCheckerImpl
import com.cililing.harvbox.thingsapp.model.CurrentValuesProvider
import com.cililing.harvbox.thingsapp.model.CurrentValuesProviderImpl
import com.cililing.harvbox.thingsapp.settings.settingsScope
import com.cililing.harvbox.thingsapp.stats.statsScope
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule(isDebug: Boolean): Module {
    return module {
        single {
            if (isDebug) {
                DebugLogger()
            } else {
                ReleaseLogger()
            } // as AppLogger
        }

        // Tools
        factory {  params ->
            ProcuderScheduler(
                    params[0],
                    params[1]
            )
        }

        // Fragments scopes.
        introScope(this)
        mainScope(this)
        dashboardScope(this)
        statsScope(this)
        settingsScope(this)

        // Model
        factory {
            ConnectivityCheckerImpl() as ConnectivityChecker
        }

        single {
            CurrentValuesProviderImpl() as CurrentValuesProvider
        }
    }
}