package com.cililing.harvbox.thingsapp.core

import android.content.Context
import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.common.DebugLogger
import com.cililing.harvbox.common.ReleaseLogger
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.AppControllerImpl
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.dashboard.dashboardScope
import com.cililing.harvbox.thingsapp.intro.introScope
import com.cililing.harvbox.thingsapp.main.mainScope
import com.cililing.harvbox.thingsapp.model.ConnectivityCheckerImpl
import com.cililing.harvbox.thingsapp.settings.settingsScope
import com.cililing.harvbox.thingsapp.gallery.galleryScope
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun appModule(isDebug: Boolean, context: Context): Module {
    return module {
        single {
            if (isDebug) {
                DebugLogger()
            } else {
                ReleaseLogger()
            } // as Logger
        }

        single {
            AppControllerImpl(context) as AppController
        }

        single {
            Gson()
        }

        // Providers
        single(named<StatusSnapshot>()) {
            CurrentSnapshotProviderImpl<StatusSnapshot>() as CurrentSnapshotProvider<StatusSnapshot>
        }
        single(named(ProvidersIds.LIGHT_1)) {
            CurrentSnapshotProviderImpl<Set<LightTrigger>>() as CurrentSnapshotProvider<Set<LightTrigger>>
        }
        single(named(ProvidersIds.LIGHT_2)) {
            CurrentSnapshotProviderImpl<Set<LightTrigger>>() as CurrentSnapshotProvider<Set<LightTrigger>>
        }
        single(named(ProvidersIds.ELASTIC_COOLDOWN)) {
            CurrentSnapshotProviderImpl<Long>() as CurrentSnapshotProvider<Long>
        }
        single(named(ProvidersIds.REALTIME_DB_COOLDOWN)) {
            CurrentSnapshotProviderImpl<Long>() as CurrentSnapshotProvider<Long>
        }
        single(named(ProvidersIds.LAST_PHOTO)) {
            CurrentSnapshotProviderImpl<String>() as CurrentSnapshotProvider<String>
        }
        single(named(ProvidersIds.PHOTO_COOLDOWN)) {
            CurrentSnapshotProviderImpl<Long>() as CurrentSnapshotProvider<Long>
        }

        // Tools
        factory { params ->
            ProducerScheduler(
                params[0],
                params[1]
            )
        }

        single {
            LightStatusHandlerImpl(
                get(),
                get(named(ProvidersIds.LIGHT_1)),
                get(named(ProvidersIds.LIGHT_2))
            ) as LightStatusHandler
        }

        factory {
            object : Clock {} as Clock
        }

        // Fragments scopes.
        introScope(this)
        mainScope(this)
        dashboardScope(this)
        galleryScope(this)
        settingsScope(this)

        // Model
        factory {
            ConnectivityCheckerImpl() as ConnectivityChecker
        }
        single(createdAtStart = true) {
            FirebaseRealtimeDatabaseImpl(
                FirebaseApp.getInstance(),
                get(named(ProvidersIds.LIGHT_1)),
                get(named(ProvidersIds.LIGHT_2)),
                get(named(ProvidersIds.ELASTIC_COOLDOWN)),
                get(named(ProvidersIds.REALTIME_DB_COOLDOWN)),
                get(named(ProvidersIds.LAST_PHOTO)),
                get(named(ProvidersIds.PHOTO_COOLDOWN)),
                get(),
                get()
            ) as FirebaseRealtimeDatabase
        }
        single(createdAtStart = true) {
            FirebaseCloudDatabaseImpl(
                FirebaseApp.getInstance()
            ) as FirebaseCloudDatabase
        }
    }
}