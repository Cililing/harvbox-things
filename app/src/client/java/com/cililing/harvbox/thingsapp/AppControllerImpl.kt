package com.cililing.harvbox.thingsapp

import com.cililing.client.ClientService
import com.cililing.client.ClientServiceImpl
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.model.LightTrigger
import com.google.firebase.FirebaseApp

class AppControllerImpl : AppController {

    private val clientService by lazy {
        ClientServiceImpl(
                FirebaseApp.getInstance(),
                AppController.isDebug
        ) as ClientService
    }

    override suspend fun getData(): StatusSnapshot {
        return clientService.getCurrentSnapshot()
    }

    override fun request(actionRequest: ThingsActionRequest) {
        clientService.request(actionRequest)
    }

    override fun newLightSettingsReceived(light1: Set<LightTrigger>, light2: Set<LightTrigger>) {
        // This is implemented only in direct mode.
    }
}