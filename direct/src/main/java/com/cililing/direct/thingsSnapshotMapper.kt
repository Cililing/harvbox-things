package com.cililing.direct

import com.cililing.direct.firebase.reporting.FirebaseThingsSnapshot
import com.cililing.direct.firebase.reporting.PowerOn
import com.cililing.direct.firebase.reporting.valueOf
import com.cililing.harvbox.thingsapp.thingscontroller.MockingTool
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsSnapshot
import kotlinx.coroutines.runBlocking

internal fun ThingsSnapshot.toFirebaseThingsSnapshot(): FirebaseThingsSnapshot {
    return FirebaseThingsSnapshot(
            light1PowerOn = this.twoRelaySnapshot.relay1Snapshot.gpioState.let { PowerOn(it) },
            light2PowerOn = this.twoRelaySnapshot.relay2Snapshot.gpioState.let { PowerOn(it) },
            proximityValue = this.proximitySnapshot.value.let { valueOf(it, 0.0) },
            humidityValue = this.ads1015Snapshot.a0.value.let { valueOf(it, 0) },
            tempValue = this.ads1015Snapshot.a1.value.let { valueOf(it, 0) }
    )
}

internal fun main() {
    (0..100).forEach {
        runBlocking {
            repeat(100) {
                MockingTool.proxyToSnapshot().invoke().let {
                    println(it.toFirebaseThingsSnapshot())
                }
            }
        }
    }
}