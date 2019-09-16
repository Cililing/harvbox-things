package com.cililing.harvbox.thingsapp.dashboard

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.scope.currentScope

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : BaseFragment<DashboardContract.Presenter>(), DashboardContract.View {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_dashboard

    override val presenter: DashboardContract.Presenter by currentScope.inject {
        createPresenterParams(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView?.setBackgroundColor(Color.RED)
    }
}
