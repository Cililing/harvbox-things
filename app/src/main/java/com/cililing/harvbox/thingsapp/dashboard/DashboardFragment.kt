package com.cililing.harvbox.thingsapp.dashboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.ProducerScheduler
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.model.LightStatus
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.find
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
                get(),
                get { ProducerScheduler.createKoinParams(5000, null) },
                get()
        )
    }

    override fun onResume() {
        super.onResume()
    }

    private val temperatureView by lazy { find<TextView>(R.id.dashboard_temperature) }
    private val lightStatusView by lazy { find<TextView>(R.id.dashboard_light_status) }

    private val dashboardView by lazy { find<TextView>(R.id.dashboard_snapshot) }

    private val relay1Switcher by lazy { find<View>(R.id.relay_1) }
    private val relay2Switcher by lazy { find<View>(R.id.relay_2) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        relay1Switcher.onClick {
            presenter.onLight1Click()
        }

        relay2Switcher.onClick {
            presenter.onLight2Click()
        }
    }

    override fun onNewTemperatureReceived(new: Float) {
        temperatureView.text = new.toString()
    }

    override fun onNewLightStatusReceived(new: LightStatus) {
        lightStatusView.text = new.toString()
    }

    override fun onNewSnapshot(new: StatusSnapshot) {
        dashboardView.text = new.toString()
    }
}
