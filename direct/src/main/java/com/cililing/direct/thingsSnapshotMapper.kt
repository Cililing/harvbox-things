package com.cililing.direct

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.PowerOn
import com.cililing.harvbox.common.valueOf
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsSnapshot

internal fun ThingsSnapshot.toFirebaseThingsSnapshot(): StatusSnapshot {
    return StatusSnapshot(
            light1PowerOn = this.twoRelaySnapshot.relay1Snapshot.gpioState.let { PowerOn(it) },
            light2PowerOn = this.twoRelaySnapshot.relay2Snapshot.gpioState.let { PowerOn(it) },
            proximityValue = this.proximitySnapshot.value.let { valueOf(it, 0.0) },
            humidityValue = this.ads1015Snapshot.a0.value.let { valueOf(it, 0) },
            tempValue = this.ads1015Snapshot.a1.value.let { valueOf(it, 0) }
    )
}