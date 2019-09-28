package com.cililing.harvbox.thingsapp.settings

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ViewAnimator
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.find
import org.koin.android.scope.currentScope

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
        getPresenterParams(this)
    }

    private val appSettingsButton by lazy { find<Button>(R.id.fragment_settings_menu_app) }
    private val lightSettingsButton by lazy { find<Button>(R.id.fragment_settings_menu_light) }
    private val aboutAppButton by lazy { find<Button>(R.id.fragment_settings_menu_about_app) }
    private val contentFrameAnimator by lazy { find<ViewAnimator>(R.id.fragment_settings_content_animator) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appSettingsButton.onClick { presenter.onAppSettingsClicked() }
        lightSettingsButton.onClick { presenter.onLightSettingsClicked() }
        aboutAppButton.onClick { presenter.onShowAboutAppClicked() }
    }

    override fun showAppSettings() = contentFrameAnimator.showAppSettings()
    override fun showLightSettings() = contentFrameAnimator.showLightSettings()
    override fun showAboutApp() = contentFrameAnimator.showAboutApp()
}