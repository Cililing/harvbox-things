package com.cililing.harvbox.thingsapp.model

interface CurrentValuesProvider {
    suspend fun requestTemperature(): Float
    suspend fun requestLightStatus(): LightStatus
}