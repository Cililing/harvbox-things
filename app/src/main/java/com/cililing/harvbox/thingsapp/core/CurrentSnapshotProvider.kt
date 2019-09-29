package com.cililing.harvbox.thingsapp.core

interface CurrentSnapshotProvider<T> {
    interface Listener<T> {
        fun onNewSnapshot(snapshot: T)
    }

    fun registerListener(listener: Listener<T>)
    fun unregisterListener(listener: Listener<T>)
    fun newSnapshotAvailable(statusSnapshot: T)
}

class CurrentSnapshotProviderImpl<T> : CurrentSnapshotProvider<T> {

    private var current: T? = null

    private val listeners = mutableSetOf<CurrentSnapshotProvider.Listener<T>>()

    override fun registerListener(listener: CurrentSnapshotProvider.Listener<T>) {
        listeners.add(listener)
        current?.let { listener.onNewSnapshot(it) }
    }

    override fun unregisterListener(listener: CurrentSnapshotProvider.Listener<T>) {
        listeners.remove(listener)
    }

    override fun newSnapshotAvailable(statusSnapshot: T) {
        current = statusSnapshot
        listeners.forEach { it.onNewSnapshot(statusSnapshot) }
    }
}