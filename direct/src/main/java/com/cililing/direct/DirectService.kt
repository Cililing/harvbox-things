package com.cililing.direct

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.google.firebase.FirebaseApp

interface DirectService {
    suspend fun getAndProcess(): StatusSnapshot
    fun request(actionRequest: ThingsActionRequest)
    fun release()
}

fun getDirectService(
    firebaseApp: FirebaseApp,
    isDebug: Boolean
): DirectService {
    return DirectServiceImpl(firebaseApp, isDebug)
}
