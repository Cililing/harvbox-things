package com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.Controller

interface StateController : Controller<StateSnapshot> {
    fun getState(): Boolean
    fun setState(value: Boolean)
    fun changeState()
}
