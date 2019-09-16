package com.cililing.harvbox.thingsapp.model

import kotlinx.coroutines.delay
import java.util.*

class CurrentValuesProviderImpl : CurrentValuesProvider {

    private val random = Random()

    override suspend fun requestTemperature(): Float {
        delay(1000)
        return random.nextFloat()
    }

    override suspend fun requestLightStatus(): LightStatus {
        delay(1320)
        return LightStatus(
                random.nextBoolean(),
                random.nextBoolean()
        )
    }

}