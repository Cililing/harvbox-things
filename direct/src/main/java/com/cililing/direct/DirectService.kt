package com.cililing.direct

import com.cililing.harvbox.common.StatusSnapshot
import com.google.firebase.FirebaseApp

interface DirectService {
    suspend fun getAndProcess(): StatusSnapshot
}

fun getDirectService(
        firebaseApp: FirebaseApp,
        isDebug: Boolean
): DirectService {
    return DirectServiceImpl(firebaseApp, isDebug)
}
