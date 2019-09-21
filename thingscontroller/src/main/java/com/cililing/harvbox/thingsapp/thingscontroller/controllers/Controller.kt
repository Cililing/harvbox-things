package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import java.io.Serializable

interface Controller<S: Serializable> {
    val parent: Controller<*>?
    fun getSnapshot(): S
    fun release()
}

