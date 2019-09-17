package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import com.cililing.harvbox.thingsapp.thingscontroller.controllers.generic.StateController
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

internal class TwoRelayControllerImpl(gpio1: String,
                                      gpio2: String,
                                      override val parent: Controller<*>? = null
) : TwoRelayController, KoinComponent {

    override val relay1: StateController by inject { parametersOf(gpio1, false, this) }
    override val relay2: StateController by inject { parametersOf(gpio2, false, this) }

    override fun release() {
        relay1.release()
        relay2.release()
    }

    override fun getSnapshot(): TwoRelaySnapshot {
        return TwoRelaySnapshot(
                relay1.getSnapshot(),
                relay2.getSnapshot()
        )
    }
}