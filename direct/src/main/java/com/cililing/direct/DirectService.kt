package com.cililing.direct

import android.content.Context
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.google.firebase.FirebaseApp

interface DirectService {
    suspend fun getAndProcess(): StatusSnapshot
    fun request(actionRequest: ThingsActionRequest)
    fun scheduleTasks(tasks: List<ExactTimeScheduleTask>)
    fun setElasticCooldown(cooldown: Long)
    fun release()
}

fun getDirectService(
    context: Context,
    firebaseApp: FirebaseApp,
    cooldown: Long,
    isDebug: Boolean
): DirectService {
    return DirectServiceImpl(context, firebaseApp, cooldown, isDebug)
}
