package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Snapshot
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04Snapshot
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelaySnapshot
import java.io.Serializable

data class ThingsSnapshot(
    val twoRelaySnapshot: TwoRelaySnapshot,
    val proximitySnapshot: HCSR04Snapshot,
    val ads1015Snapshot: ADS1015Snapshot
) : Serializable