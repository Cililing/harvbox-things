package com.cililing.harvbox.thingsapp

import com.cililing.harvbox.common.StatusSnapshot

interface AppController {
    companion object {
        val isDebug = BuildConfig.DEBUG
    }

    suspend fun getData(): StatusSnapshot
}