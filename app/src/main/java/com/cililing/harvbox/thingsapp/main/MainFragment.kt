package com.cililing.harvbox.thingsapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.scope.currentScope

class MainFragment : BaseFragment<MainContract.Presenter>(), MainContract.View {

    override val presenter: MainContract.Presenter by currentScope.inject {
        getPresenterParams(this)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}