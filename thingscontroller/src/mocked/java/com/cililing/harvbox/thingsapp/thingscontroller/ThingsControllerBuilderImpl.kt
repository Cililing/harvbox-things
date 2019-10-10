package com.cililing.harvbox.thingsapp.thingscontroller

import android.content.Context

class ThingsControllerBuilderImpl : ThingsControllerBuilder {
    override fun build(
        context: Context,
        photoListener: (ByteArray) -> Unit
    ): ThingsController {
        return ThingsControllerImpl(
            parent = null,
            photoListener = photoListener,
            context = context
        )
    }
}