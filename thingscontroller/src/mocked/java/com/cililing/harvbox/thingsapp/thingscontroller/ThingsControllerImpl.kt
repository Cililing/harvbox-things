package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.ADS1015Snapshot
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.HCSR04Snapshot
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.TwoRelaySnapshot
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateSnapshot
import kotlin.random.Random

class ThingsControllerImpl(override val parent: Controller<*>?) : ThingsController {

    var relay1State = StateSnapshot(false)
    var relay2State = StateSnapshot(false)

    override fun setState(state: RequiredThingsState) {
        relay1State = relay1State.copy(gpioState = state.relay1)
        relay2State = relay2State.copy(gpioState = state.relay2)
    }

    override fun setState(
        relay1: Boolean?,
        relay2: Boolean?
    ) {
        relay1?.let { relay1State = relay1State.copy(gpioState = it) }
        relay2?.let { relay2State = relay2State.copy(gpioState = it) }
    }

    var proximityValue: Double? = null
    var a1Value: Double? = null
    var a2Value: Double? = null

    override fun getSnapshot(): ThingsSnapshot {
        proximityValue = throttleDouble(
                proximityValue ?: 0.0,
                0..22
        )
        a1Value = throttleDouble(
                a1Value ?: 24.0,
                22..28
        )
        a2Value = throttleDouble(
                a2Value ?: 18.0,
                10..30
        )
        return ThingsSnapshot(
                twoRelaySnapshot = TwoRelaySnapshot(
                        relay1Snapshot = relay1State,
                        relay2Snapshot = relay2State
                ),
                proximitySnapshot = HCSR04Snapshot(
                        value = proximityValue
                ),
                ads1015Snapshot = ADS1015Snapshot(
                        a0 = ADS1015Controller.ADS1015PinSnapshot(a1Value),
                        a1 = ADS1015Controller.ADS1015PinSnapshot(a2Value)
                )
        )
    }

    private fun throttleInt(start: Int, boundaries: IntRange): Int {
        val boundariesLenght = boundaries.last - boundaries.first
        val maxBoundaryThrottle = (0.1) * boundariesLenght

        val suggestedValue = start + (boundariesLenght * maxBoundaryThrottle).toInt()
        return if (boundaries.contains(suggestedValue)) {
            suggestedValue
        } else {
            if (suggestedValue > boundaries.last) {
                boundaries.last
            } else {
                boundaries.first
            }
        }
    }

    private fun throttleDouble(start: Double, boundaries: IntRange): Double {
        return throttleInt(start.toInt(), boundaries)
                .plus(Random.nextDouble(-1.0, 1.0))
    }

    override fun release() {
    }

    override fun requestPhoto(): Boolean {
        return true
    }
}