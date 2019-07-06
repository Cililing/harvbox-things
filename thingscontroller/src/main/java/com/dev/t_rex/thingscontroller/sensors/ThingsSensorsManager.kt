package com.dev.t_rex.thingscontroller.sensors

/**
 * Created by t-rex on 09/07/2018.
 */
interface ThingsSensorsManager {

    fun getAvailableSensors(): List<ThingsSensor>

    fun getRegisteredSensors(): List<ThingsSensorType>
    fun registerSensor(type: ThingsSensorType): Boolean
    fun unregisterSensor(type: ThingsSensorType): Boolean
}