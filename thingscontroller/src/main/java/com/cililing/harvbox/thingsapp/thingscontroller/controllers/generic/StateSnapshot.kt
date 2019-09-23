package com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic

import java.io.Serializable

data class StateSnapshot(
    val gpioState: Boolean
) : Serializable