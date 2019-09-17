package com.cililing.harvbox.thingsapp.thingscontroller

import kotlinx.coroutines.runBlocking

fun main() {

    val thingsController = ThingsControllerBuilderImpl().build()

    runBlocking {
        repeat(100) {
            println(thingsController.getSnapshotAsync())
        }
    }
}