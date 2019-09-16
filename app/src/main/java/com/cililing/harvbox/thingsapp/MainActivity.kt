package com.cililing.harvbox.thingsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cililing.harvbox.thingsapp.core.mvp.BaseFragment
import com.cililing.harvbox.thingsapp.intro.IntroFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = IntroFragment.newInstance()
        replaceOrInjectFragment(fragment)
    }

    fun replaceOrInjectFragment(fragment: BaseFragment<*>) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.content_frame, fragment)
            commit()
        }
    }
}
