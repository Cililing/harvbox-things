package com.cililing.harvbox.thingsapp.thingscontroller

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KSuspendFunction0

val thingsController = ThingsControllerBuilderImpl().build()

fun main() {

    runBlocking {
        repeat(100) {
            println(thingsController.getSnapshotAsync())
        }
    }
}

object MockingTool {
    fun proxyToSnapshot(): KSuspendFunction0<ThingsSnapshot> {
        return thingsController::getSnapshotAsync
    }
}