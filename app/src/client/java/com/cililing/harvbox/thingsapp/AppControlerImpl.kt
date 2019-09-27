package com.cililing.harvbox.thingsapp

import com.cililing.client.ClientService
import com.cililing.client.ClientServiceImpl
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.google.firebase.FirebaseApp

class AppControlerImpl : AppController {

    private val clientService by lazy {
        ClientServiceImpl(
                FirebaseApp.getInstance(),
                AppController.isDebug
        ) as ClientService
    }

    override suspend fun getData(): StatusSnapshot {
        return clientService.getCurrentSnapshot()
    }

    override suspend fun request(actionRequest: ThingsActionRequest) {
        clientService.request(actionRequest)
    }
}
