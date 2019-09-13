package com.cililing.harvbox.thingsapp.core

import com.cililing.harvbox.thingsapp.dashboard.dashboardScope
import com.cililing.harvbox.thingsapp.intro.introScope
import com.cililing.harvbox.thingsapp.main.mainScope
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
            } as AppLogger
        }

        // Fragments scopes.
        introScope(this)
        mainScope(this)
        dashboardScope(this)
        settingsScope(this)
        statsScope(this)

    }
}