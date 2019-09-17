package com.cililing.harvbox.thingsapp.thingscontroller.controllers

interface HCSR04Controller : Controller<HCSR04Snapshot> {
    var distanceCallback: ((Double) -> Unit)?
}

