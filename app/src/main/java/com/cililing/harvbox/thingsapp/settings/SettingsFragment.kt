package com.cililing.harvbox.thingsapp.settings

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cililing.harvbox.common.timePickerDialog24
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.model.LightTrigger
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.find
import org.koin.android.ext.android.get
import org.koin.android.scope.currentScope
import org.koin.core.qualifier.named

class SettingsFragment : BaseFragment<SettingsContract.Presenter>(), SettingsContract.View {
    companion object {
        fun newInstance() = SettingsFragment()

        private fun ViewAnimator.showAppSettings() = this.showChild(0)
        private fun ViewAnimator.showLightSettings() = this.showChild(1)
        private fun ViewAnimator.showAboutApp() = this.showChild(2)

        private fun ViewAnimator.showChild(index: Int) {
            if (this.displayedChild != index) {
                this.displayedChild = index
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_settings

    override val presenter: SettingsContract.Presenter by currentScope.inject {
        getPresenterParams(this, get(), get(named("light1")), get(named("light2")), get())
    }

    // Menu and container
    private val appSettingsButton by lazy { find<Button>(R.id.fragment_settings_menu_app) }
    private val lightSettingsButton by lazy { find<Button>(R.id.fragment_settings_menu_light) }
    private val aboutAppButton by lazy { find<Button>(R.id.fragment_settings_menu_about_app) }
    private val contentFrameAnimator by lazy { find<ViewAnimator>(R.id.fragment_settings_content_animator) }

    private val light1Settings by lazy { LightSettingsContainer(find(R.id.container_1)) }
    private val light2Settings by lazy { LightSettingsContainer(find(R.id.container_2)) }

    private inner class LightSettingsContainer(val container: View) {
        val addTriggerButton by lazy { container.find<Button>(R.id.light_add_trigger) }
        val lightNameTextView by lazy { container.find<TextView>(R.id.light_name) }
        val lightTriggerListRecyclerView by lazy { container.find<RecyclerView>(R.id.light_trigger_list) }

        var lightTriggersAdapter: LightTriggersAdapter? = null

        fun updateTriggers(lightId: SettingsContract.LightId, triggers: List<LightTrigger>) {
            if (lightTriggersAdapter == null) {
                lightTriggerListRecyclerView.layoutManager = LinearLayoutManager(context)
                lightTriggersAdapter = LightTriggersAdapter(presenter, triggers, lightId)
                lightTriggerListRecyclerView.adapter = lightTriggersAdapter
            }

            lightTriggersAdapter?.updateTriggers(triggers)
        }

        fun setLightTitle(title: String) {
            lightNameTextView.text = title
        }

        fun setAddTriggerButtonListener(listener: (View) -> Unit) {
            addTriggerButton.setOnClickListener(listener)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appSettingsButton.onClick { presenter.onAppSettingsClicked() }
        lightSettingsButton.onClick { presenter.onLightSettingsClicked() }
        aboutAppButton.onClick { presenter.onShowAboutAppClicked() }

        with(light1Settings) {
            setLightTitle("Light1")
            setAddTriggerButtonListener {
                presenter.onNewLightTriggerClicked(SettingsContract.LightId.LIGHT_1)
            }
        }

        with(light2Settings) {
            setLightTitle("Light2")
            setAddTriggerButtonListener {
                presenter.onNewLightTriggerClicked(SettingsContract.LightId.LIGHT_2)
            }
        }
    }

    override fun showAppSettings() = contentFrameAnimator.showAppSettings()
    override fun showLightSettings() = contentFrameAnimator.showLightSettings()
    override fun showAboutApp() = contentFrameAnimator.showAboutApp()

    override fun fillTriggers(lightId: SettingsContract.LightId, triggerSet: Set<LightTrigger>) {
        val settings = when (lightId) {
            SettingsContract.LightId.LIGHT_1 -> light1Settings
            SettingsContract.LightId.LIGHT_2 -> light2Settings
        }

        settings.updateTriggers(
                lightId,
                triggerSet.toList().sortedWith(compareBy(LightTrigger::hour, LightTrigger::minute))
        )
    }

    override fun showTimePicker(lightId: SettingsContract.LightId,
                                hour: Int,
                                minute: Int) {
        timePickerDialog24(
                context,
                hour,
                minute,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->
                    presenter.onNewTriggerSelected(lightId, hourOfDay, minuteOfHour)
                }
        ).let {
            it?.setTitle(lightId.toString())
            it?.show()
        }
    }
}