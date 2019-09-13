package com.cililing.harvbox.thingsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cililing.harvbox.thingsapp.intro.IntroFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = IntroFragment.newInstance()
        injectFragment(fragment)
    }

    private fun injectFragment(fragment: Fragment) {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.content_frame, fragment)
            commit()
        }
    }
}
