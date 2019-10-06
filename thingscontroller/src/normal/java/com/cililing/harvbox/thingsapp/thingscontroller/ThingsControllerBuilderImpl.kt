package com.cililing.harvbox.thingsapp.thingscontroller

import android.content.Context

class ThingsControllerBuilderImpl : ThingsControllerBuilder {

    private var isDebug: Boolean = false
    private var configMap: ControllerConfigMap = ControllerConfigMapDefault
    private var proximityCallback: ((Double) -> Unit)? = null
    private var photoListener: ((ByteArray) -> Unit)? = null

    fun isDebug(isDebug: Boolean) = apply { this.isDebug = isDebug }
    fun configMap(configMap: ControllerConfigMap) = apply { this.configMap = configMap }

    fun withProximityListener(callback: (Double) -> Unit) = apply { proximityCallback = callback }

    override fun build(context: Context): ThingsController {
        return ThingsControllerImpl(
            configMap = configMap,
            debug = isDebug,
            photoListener = photoListener ?: {},
            context = context
        ).apply {
            proximityCallback?.let {
                this.proximityController.distanceCallback = it
            }
        }
    }
}