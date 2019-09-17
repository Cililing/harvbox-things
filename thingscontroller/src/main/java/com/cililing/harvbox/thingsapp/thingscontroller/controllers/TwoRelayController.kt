package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateController

interface TwoRelayController : Controller<TwoRelaySnapshot> {
    val relay1: StateController
    val relay2: StateController
}

