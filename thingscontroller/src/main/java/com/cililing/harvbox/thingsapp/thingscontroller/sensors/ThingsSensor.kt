package com.cililing.harvbox.thingsapp.thingscontroller.sensors

/**
 * Created by t-rex on 09/07/2018.
 */
interface ThingsSensor {
    fun getType(): ThingsSensorUnit
    fun getValue(): Double
    fun getUnit(): ThingsSensorUnit
}