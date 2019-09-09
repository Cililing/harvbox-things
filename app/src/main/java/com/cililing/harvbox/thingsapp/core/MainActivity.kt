package com.cililing.harvbox.thingsapp.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cililing.harvbox.thingsapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = DashboardFragment()
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.content_frame, fragment)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
