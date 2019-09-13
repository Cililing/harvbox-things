package com.cililing.harvbox.thingsapp.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface LifecycleController

interface LifecycleBasedComponent {
    fun onAny() {}
    fun onCreate() {}
    fun onStart() {}
    fun onResume() {}
    fun onPause() {}
    fun onStop() {}
    fun onDestroy() {}
}

fun <T : LifecycleBasedComponent> LifecycleOwner.newLifecycleComponent(component: T):
        DefaultLifecycleController {
    return DefaultLifecycleController(this, component)
}

class DefaultLifecycleController(
        private val lifecycleOwner: LifecycleOwner,
        private val component: LifecycleBasedComponent
) : LifecycleController, LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny() {
        component.onAny()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        component.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        component.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        component.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        component.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        component.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        component.onDestroy()
    }
}