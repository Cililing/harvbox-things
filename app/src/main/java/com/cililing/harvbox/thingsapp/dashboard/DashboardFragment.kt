package com.cililing.harvbox.thingsapp.dashboard

import android.os.Bundle
import android.view.View
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.customViews.LabelView
import com.cililing.harvbox.thingsapp.customViews.OnOffButton
import com.cililing.harvbox.thingsapp.customViews.RealValueView
import org.jetbrains.anko.support.v4.find
import org.koin.android.ext.android.get
import org.koin.android.scope.currentScope
import org.koin.core.qualifier.named

class DashboardFragment : BaseFragment<DashboardContract.Presenter>(), DashboardContract.View {
    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_dashboard

    override val presenter: DashboardContract.Presenter by currentScope.inject {
        createPresenterParams(
                this,
                get(),
                get(named<StatusSnapshot>())
        )
    }

    private val lastUpdateView by lazy { find<LabelView>(R.id.dashboard_last_update) }
    private val temperatureView by lazy { find<RealValueView>(R.id.dashboard_temp_value) }
    private val humidityView by lazy { find<RealValueView>(R.id.dashboard_humidity_value) }
    private val proximityView by lazy { find<RealValueView>(R.id.dashboard_proximity_value) }
    private val light1Button by lazy { find<OnOffButton>(R.id.dashboard_light_1_button) }
    private val light2Button by lazy { find<OnOffButton>(R.id.dashboard_light_2_button) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        light1Button.init("Light1", false)
        light2Button.init("Light2", false)

        temperatureView.init("Temperature", "waiting...")
        humidityView.init("Humidity", "waiting...")
        proximityView.init("Proximity", "waiting...")

        light1Button.setOnCheckedListner {
            presenter.onLight1Click(it)
        }
        light2Button.setOnCheckedListner {
            presenter.onLight2Click(it)
        }
    }

    override fun onNewLight1StatusReceived(new: Boolean) {
        light1Button.fixChecked(new)
    }

    override fun onNewLight2StatusReceived(new: Boolean) {
        light2Button.fixChecked(new)
    }

    override fun onNewSnapshotTimeReceived(new: String) {
        lastUpdateView.setLabel("Last snapshot date: $new")
    }

    override fun onNewTemperatureReceived(new: Float) {
        temperatureView.updateValue(new.toString())
    }

    override fun onNewHumidityReceived(new: Float) {
        humidityView.updateValue(new.toString())
    }

    override fun onNewProximityReceived(new: Float) {
        proximityView.updateValue(new.toString())
    }
}
