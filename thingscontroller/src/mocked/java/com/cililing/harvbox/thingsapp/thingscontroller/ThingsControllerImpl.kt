package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.*
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateSnapshot
import kotlinx.coroutines.*
import kotlin.random.Random

class ThingsControllerImpl(override val parent: Controller<*>?) : ThingsController {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
        parentJob + Dispatchers.IO
    )

    override suspend fun getSnapshotAsync(): ThingsSnapshot {
        return withContext(coroutineScope.coroutineContext) {
            getSnapshot().delayAndReturn(Random.nextLong(2000))
        }
    }

    override fun release() {
        parentJob.cancel()
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

    private suspend fun <T> T.delayAndReturn(howLong: Long = 500): T {
        delay(howLong)
        return this
    }}