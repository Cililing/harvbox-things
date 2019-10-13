package com.cililing.harvbox.thingsapp

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.core.LightTrigger

interface AppController {
    companion object {
        val isDebug = BuildConfig.DEBUG
    }

    suspend fun getData(): StatusSnapshot
    fun request(actionRequest: ThingsActionRequest)

    fun newLightSettingsReceived(light1: Set<LightTrigger>, light2: Set<LightTrigger>)
    fun newElasticCooldownReceived(cooldown: Long)

    fun requestPhoto()
}