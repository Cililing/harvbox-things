package com.dev.t_rex.thingscontroller.sensors

/**
 * Created by t-rex on 09/07/2018.
 */
interface ThingsSensor {
    fun getType(): ThingsSensorUnit
    fun getValue(): Double
    fun getUnit(): ThingsSensorUnit
}