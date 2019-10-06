package com.cililing.harvbox.thingsapp

import android.app.Application
import com.cililing.harvbox.thingsapp.core.appModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ThingsApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initFirebaseApp()
        flipTheKoin()
    }

    private fun initFirebaseApp() {
        FirebaseApp.initializeApp(this)
    }

    private fun flipTheKoin() {
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@ThingsApp)
            modules(appModule(
                    isDebug = BuildConfig.DEBUG,
                context = applicationContext
            ))
        }
    }
}