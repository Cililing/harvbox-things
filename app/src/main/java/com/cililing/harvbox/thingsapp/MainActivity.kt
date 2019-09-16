package com.cililing.harvbox.thingsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.intro.IntroFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applyAppTheme()

        val fragment = IntroFragment.newInstance()
        replaceOrInjectFragment(fragment)
    }

    private fun applyAppTheme() {
        // Because of some reason changing app style to `NoActionBar` child doesn't
        // remove action bar. This is a working workaround.
        // There is a chance that the reason of such a behaviour is a bug in AndroidX
        // library, as before support to X migration everything worked OK.
        supportActionBar?.hide()
        actionBar?.hide()
    }

    fun replaceOrInjectFragment(fragment: BaseFragment<*>) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.content_frame, fragment)
            commit()
        }
    }
}
