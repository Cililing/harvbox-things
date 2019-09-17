package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import java.io.Serializable

interface AsyncController<S: Serializable>: Controller<S> {
    suspend fun getSnapshotAsync(): S
}