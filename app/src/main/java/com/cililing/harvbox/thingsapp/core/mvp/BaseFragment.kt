package com.cililing.harvbox.thingsapp.core.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.cililing.harvbox.thingsapp.MainActivity

abstract class BaseFragment<Presenter : BasePresenter<*>> : Fragment(), BaseView<Presenter> {

    protected var rootView: View? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    fun replaceFragment(fragment: BaseFragment<*>) {
        (this.activity as MainActivity).replaceOrInjectFragment(fragment)
    }

}