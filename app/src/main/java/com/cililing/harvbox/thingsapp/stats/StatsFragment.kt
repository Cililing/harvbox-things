package com.cililing.harvbox.thingsapp.stats

import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.scope.currentScope

class StatsFragment : BaseFragment<StatsContract.Presenter>(), StatsContract.View {

    companion object {
        fun newInstance() = StatsFragment()
    }

    override val presenter: StatsContract.Presenter by currentScope.inject {
        getPresenterParams(this)
    }
}