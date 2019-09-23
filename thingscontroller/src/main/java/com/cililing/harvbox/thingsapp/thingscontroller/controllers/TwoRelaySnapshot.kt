package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateSnapshot
import java.io.Serializable

data class TwoRelaySnapshot(
    val relay1Snapshot: StateSnapshot,
    val relay2Snapshot: StateSnapshot
) : Serializable