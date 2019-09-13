package com.cililing.harvbox.thingsapp.settings

import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.ext.android.inject

class SettingsFragment : BaseFragment<SettingsContract.Presenter>(), SettingsContract.View {
    companion object {
        fun newInstance() = SettingsFragment()
    }

    override val presenter: SettingsContract.Presenter by inject {
        getPresenterParams(this)
    }
}