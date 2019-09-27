package com.cililing.client

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest

interface ClientService {
    fun request(actionRequest: ThingsActionRequest)
    fun getCurrentSnapshot(): StatusSnapshot
}