package com.cililing.harvbox.thingsapp

import com.cililing.harvbox.common.FirebaseThingsSnapshot

interface AppController {
    companion object {
        val isDebug = BuildConfig.DEBUG
    }

    suspend fun getData(): FirebaseThingsSnapshot
}