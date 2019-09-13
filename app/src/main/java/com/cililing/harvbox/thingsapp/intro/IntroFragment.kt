package com.cililing.harvbox.thingsapp.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.scope.currentScope

class IntroFragment : BaseFragment<IntroContract.Presenter>(), IntroContract.View {

    override val presenter: IntroContract.Presenter by currentScope.inject {
        getPresenterParams(this)
    }

    companion object {
        fun newInstance() = IntroFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

}