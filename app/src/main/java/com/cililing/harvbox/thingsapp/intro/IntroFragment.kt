package com.cililing.harvbox.thingsapp.intro

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ViewAnimator
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.main.MainFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.find
import org.koin.android.ext.android.get
import org.koin.android.scope.currentScope
import org.koin.core.context.GlobalContext.get

class IntroFragment : BaseFragment<IntroContract.Presenter>(), IntroContract.View {

    private val mainViewAnimator by lazy { find<ViewAnimator>(R.id.fragment_intro_view_animator) }
    private val retryButton by lazy { find<Button>(R.id.network_error_retry_button) }

    private enum class ViewAnimatorChild(
        private val child: Int
    ) {
        PROGRESS(0),
        ERROR(1);

        fun showView(viewAnimator: ViewAnimator) {
            if (viewAnimator.displayedChild != child) {
                viewAnimator.displayedChild = child
            }
        }
    }

    override val presenter: IntroContract.Presenter by currentScope.inject {
        getPresenterParams(this, get())
    }

    companion object {
        fun newInstance() = IntroFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_intro

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retryButton.onClick {
            presenter.onRetryClick()
        }
    }

    override fun showProgress() {
        ViewAnimatorChild.PROGRESS.showView(mainViewAnimator)
    }

    override fun showNetworkError() {
        ViewAnimatorChild.ERROR.showView(mainViewAnimator)
    }

    override fun goToMainFragment() {
        replaceFragment(MainFragment.newInstance())
    }
}