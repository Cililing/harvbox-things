package com.cililing.harvbox.thingsapp.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ViewAnimator
import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.ProvidersIds
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.customViews.LabelView
import com.cililing.harvbox.thingsapp.customViews.LightSwitchButton
import com.cililing.harvbox.thingsapp.customViews.RealValueView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.find
import org.koin.android.ext.android.get
import org.koin.android.scope.currentScope
import org.koin.core.qualifier.named
import java.lang.Exception

class DashboardFragment : BaseFragment<DashboardContract.Presenter>(), DashboardContract.View {
    companion object {
        fun newInstance() = DashboardFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_dashboard

    override val presenter: DashboardContract.Presenter by currentScope.inject {
        createPresenterParams(
                this,
                get(),
                get(named<StatusSnapshot>()),
                get(named(ProvidersIds.LAST_PHOTO)),
                get()
        )
    }

    private val lastUpdateView by lazy { find<LabelView>(R.id.dashboard_last_update) }
    private val temperatureView by lazy { find<RealValueView>(R.id.dashboard_temp_value) }
    private val humidityView by lazy { find<RealValueView>(R.id.dashboard_humidity_value) }
    private val proximityView by lazy { find<RealValueView>(R.id.dashboard_proximity_value) }
    private val light1Button by lazy { find<LightSwitchButton>(R.id.dashboard_light_1_button) }
    private val light2Button by lazy { find<LightSwitchButton>(R.id.dashboard_light_2_button) }
    private val requestPhotoButton by lazy { find<Button>(R.id.dashboard_request_photo_button) }

    private val photoContainer by lazy { find<ViewAnimator>(R.id.dashboard_photo_container) }
    private val photoThumb by lazy { find<ImageView>(R.id.dashboard_photo_thumb) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        light1Button.init("Light1", false)
        light2Button.init("Light2", false)

        temperatureView.init("Temperature", "waiting...")
        humidityView.init("Humidity", "waiting...")
        proximityView.init("Proximity", "waiting...")

        light1Button.setOnCheckedListener {
            presenter.onLight1Click(it)
        }
        light2Button.setOnCheckedListener {
            presenter.onLight2Click(it)
        }
        requestPhotoButton.onClick {
            presenter.onRequestPhotoClicked()
        }
    }

    override fun onNewPhotoReceived(new: String?) {
        if (new.isNullOrEmpty()) {
            photoContainer.displayedChild = 1 // error
            return
        }

        Picasso.get().load(new).into(photoThumb, object : Callback {
            override fun onSuccess() {
                photoContainer.displayedChild = 0 // content
            }

            override fun onError(e: Exception?) {
                // TODO: Handle error on photo loading.
            }
        })
    }

    override fun onNewLight1StatusReceived(new: Boolean,
                                           isInRequiredState: Boolean) {
        light1Button.fixChecked(new)
        light1Button.showWarning(!isInRequiredState)
    }

    override fun onNewLight2StatusReceived(new: Boolean,
                                           isInRequiredState: Boolean) {
        light2Button.fixChecked(new)
        light2Button.showWarning(!isInRequiredState)
    }

    override fun onNewSnapshotTimeReceived(new: String) {
        lastUpdateView.setLabel(getString(R.string.last_update_date, Clock.parseFirebaseDate(new)))
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
