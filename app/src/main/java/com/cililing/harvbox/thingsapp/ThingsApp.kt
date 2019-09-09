package com.cililing.harvbox.thingsapp

import android.app.Application

class ThingsApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    val appComponent: AppComponent = AppComponentImpl()
}