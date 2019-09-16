package com.cililing.harvbox.thingsapp.main

import androidx.annotation.StringRes
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.dashboard.DashboardFragment
import com.cililing.harvbox.thingsapp.settings.SettingsFragment
import com.cililing.harvbox.thingsapp.stats.StatsFragment
import java.lang.IllegalArgumentException

sealed class Tab(
        @StringRes val name: Int,
        val creator: () -> BaseFragment<*>
) {
    companion object {
        fun resolveTabByMenuId(id: Int): Tab {
            return when (id) {
                R.id.menu_dashboard -> Dashboard
                R.id.menu_stats -> Stats
                R.id.menu_settings -> Settings
                else -> throw IllegalArgumentException("Unknown id")
            }
        }
    }

    object Dashboard : Tab(
            name = R.string.menu_dashboard,
            creator = {
                DashboardFragment.newInstance()
            }
    )

    object Stats : Tab(
            name = R.string.menu_stats,
            creator = {
                StatsFragment.newInstance()
            }
    )

    object Settings : Tab(
            name = R.string.menu_settings,
            creator = {
                SettingsFragment.newInstance()
            }
    )
}