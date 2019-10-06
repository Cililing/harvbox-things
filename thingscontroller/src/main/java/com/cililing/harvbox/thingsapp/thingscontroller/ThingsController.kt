package com.cililing.harvbox.thingsapp.thingscontroller

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller

interface ThingsController : Controller<ThingsSnapshot> {
    fun setState(state: RequiredThingsState)
    fun setState(relay1: Boolean?, relay2: Boolean?)

    fun requestPhoto(): Boolean
    val photoListener: (ByteArray) -> Unit
}