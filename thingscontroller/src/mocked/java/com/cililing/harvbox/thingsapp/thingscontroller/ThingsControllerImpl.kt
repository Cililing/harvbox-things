package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.*
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateSnapshot
import kotlin.random.Random

class ThingsControllerImpl(override val parent: Controller<*>?) : ThingsController {

    var relay1State = StateSnapshot(false)
    var relay2State = StateSnapshot(false)

    override fun setState(state: RequiredThingsState) {
        relay1State = relay1State.copy(gpioState = state.relay1)
        relay2State = relay2State.copy(gpioState = state.relay2)
    }

    override fun getSnapshot(): ThingsSnapshot {
        return ThingsSnapshot(
                twoRelaySnapshot = TwoRelaySnapshot(
                        relay1Snapshot = relay1State,
                        relay2Snapshot = relay2State
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

    override fun release() {
    }

}