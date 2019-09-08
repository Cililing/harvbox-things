package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateController
import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateControllerImpl

interface TwoRelayController : Controller {
    val relay1: StateController
    val relay2: StateController
}

internal class TwoRelayControllerImpl(gpio1: String,
                                      gpio2: String) : TwoRelayController {
    override val relay1: StateController = StateControllerImpl(gpio1)
    override val relay2: StateController = StateControllerImpl(gpio2)

    override fun release() {
        relay1.release()
        relay2.release()
    }
}