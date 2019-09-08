package com.cililing.harvbox.thingsapp.thingscontroller

interface ControllerConfigMap {
    val proximitySensorTrig: String
    val proximitySensorEcho: String

    val relayIn1: String
    val relayIn2: String

    val adcI2C: String
    val adcAdrr: Int

    val humiditySensorAdcPin: Int
    val tempSensorAdcPin: Int
}

object ControllerConfigMapDefault: ControllerConfigMap {
    override val proximitySensorEcho: String = "GPIO2_IO03"
    override val proximitySensorTrig: String = "GPIO1_IO10"

    override val relayIn1: String = "GPIO6_IO15"
    override val relayIn2: String = "GPIO6_IO14"

    override val adcI2C: String = "I2C1"
    override val adcAdrr: Int = 0x48

    override val humiditySensorAdcPin: Int = 0
    override val tempSensorAdcPin: Int = 1
}