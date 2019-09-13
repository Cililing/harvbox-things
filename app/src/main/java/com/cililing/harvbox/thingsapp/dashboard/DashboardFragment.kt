package com.cililing.harvbox.thingsapp.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : BaseFragment<DashboardContract.Presenter>(), DashboardContract.View {

    override val presenter: DashboardContract.Presenter by currentScope.inject {
        createParams(this)
    }

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dashboard_fragment, container, false)
        return rootView
    }

}
