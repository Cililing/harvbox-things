package com.cililing.harvbox.thingsapp.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cililing.harvbox.thingsapp.R
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import org.jetbrains.anko.support.v4.find
import org.koin.android.scope.currentScope

class MainFragment : BaseFragment<MainContract.Presenter>(), MainContract.View {

    companion object {
        fun newInstance() = MainFragment()
    }

    override val presenter: MainContract.Presenter by currentScope.inject {
        getPresenterParams(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_main

    private val viewPager by lazy { find<ViewPager>(R.id.main_view_pager) }
    private val navBar by lazy { find<BottomNavigationView>(R.id.main_nav_bar) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = getAdapter()
        viewPager.beginFakeDrag() // disable scrolling
        viewPager.offscreenPageLimit = presenter.tabs.size
        navBar.setOnNavigationItemSelectedListener {
            presenter.tabSelected(Tab.resolveTabByMenuId(it.itemId))
            true
        }
    }

    override fun showTab(tab: Tab) {
        viewPager.currentItem = presenter.tabs.indexOf(tab)
    }

    private fun getAdapter() : FragmentStatePagerAdapter {
        val tabs = presenter.tabs
        return object : FragmentStatePagerAdapter(childFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            override fun getItem(position: Int): Fragment {
                return tabs[position].creator.invoke()
            }

            override fun getCount(): Int {
                return tabs.count()
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return context?.getString(tabs[position].name)
            }
        }
    }
}