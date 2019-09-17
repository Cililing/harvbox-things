package com.cililing.harvbox.thingsapp.dashboard

import android.widget.TextView
import com.cililing.direct.FirebaseService
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.ProcuderScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.model.LightStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.get
import org.koin.android.scope.currentScope

class DashboardFragment : BaseFragment<DashboardContract.Presenter>(), DashboardContract.View {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_dashboard

    override val presenter: DashboardContract.Presenter by currentScope.inject {
        createPresenterParams(
                this,
                get { ProcuderScheduler.createKoinParams(5000, null) },
                get()
        )
    }

    override fun onResume() {
        super.onResume()

        GlobalScope.launch {

            FirebaseService().getSnapshotAsync().let {
                runOnUiThread {
                    toast(it)
                }
            }
        }
    }

    private val temperatureView by lazy { find<TextView>(R.id.dashboard_temperature) }
    private val lightStatusView by lazy { find<TextView>(R.id.dashboard_light_status) }


    override fun onNewTemperatureReceived(new: Float) {
        temperatureView.text = new.toString()
    }

    override fun onNewLightStatusReceived(new: LightStatus) {
        lightStatusView.text = new.toString()
    }
}
