package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.*
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateSnapshot
import kotlin.random.Random

class ThingsControllerImpl(override val parent: Controller<*>?) : ThingsController {
    override fun release() {
    }

    override fun getSnapshot(): ThingsSnapshot {
        return ThingsSnapshot(
                twoRelaySnapshot = TwoRelaySnapshot(
                        relay1Snapshot = StateSnapshot(Random.nextBoolean()),
                        relay2Snapshot = StateSnapshot(Random.nextBoolean())
                ),
                proximitySnapshot = HCSR04Snapshot(
                        value = Random.nextDouble()
                ),
                ads1015Snapshot = ADS1015Snapshot(
                        a0 = ADS1015Controller.ADS1015PinSnapshot(Random.nextInt()),
                        a1 = ADS1015Controller.ADS1015PinSnapshot(Random.nextInt())
                )
        )
    }
}