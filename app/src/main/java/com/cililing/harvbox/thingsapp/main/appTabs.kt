package com.cililing.harvbox.thingsapp.main

import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.dashboard.DashboardFragment
import com.cililing.harvbox.thingsapp.settings.SettingsFragment
import com.cililing.harvbox.thingsapp.stats.StatsFragment

sealed class Tab(
        val name: String,
        val creator: () -> BaseFragment<*>
) {
    object Dashboard : Tab(
            name = "Dashboard",
            creator = {
                DashboardFragment.newInstance()
            }
    )

    object Stats : Tab(
            name = "Stats",
            creator = {
                StatsFragment.newInstance()
            }
    )

    object Settings : Tab(
            name = "Settings",
            creator = {
                SettingsFragment.newInstance()
            }
    )
}