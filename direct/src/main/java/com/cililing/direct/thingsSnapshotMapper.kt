package com.cililing.direct

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsSnapshot

internal fun ThingsSnapshot.toFirebaseThingsSnapshot(timestamp: String? = null): StatusSnapshot {
    return StatusSnapshot(
            timestamp = timestamp,
            light1PowerOn = this.twoRelaySnapshot.relay1Snapshot.gpioState,
            light2PowerOn = this.twoRelaySnapshot.relay2Snapshot.gpioState,
            proximityValue = this.proximitySnapshot.value ?: 0.0,
            humidityValue = this.ads1015Snapshot.a0.value ?: 0.0,
            tempValue = this.ads1015Snapshot.a1.value ?: 0.0
    )
}