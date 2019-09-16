package com.cililing.harvbox.thingsapp.core.mvp

interface BasePresenter<View> {
    var view: View
    fun onResume()
    fun onPause()
    fun onDestroy()
}