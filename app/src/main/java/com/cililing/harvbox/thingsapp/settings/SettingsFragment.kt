package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope

class SettingsFragment : BaseFragment<SettingsContract.Presenter>(), SettingsContract.View {
    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override val presenter: SettingsContract.Presenter by currentScope.inject {
        getPresenterParams(this)
    }
}