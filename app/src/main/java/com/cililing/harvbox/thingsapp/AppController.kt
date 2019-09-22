package com.cililing.harvbox.thingsapp

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest

interface AppController {
    companion object {
        val isDebug = BuildConfig.DEBUG
    }

    suspend fun getData(): StatusSnapshot
    suspend fun request(actionRequest: ThingsActionRequest)
}