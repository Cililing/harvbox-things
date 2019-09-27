package com.cililing.harvbox.common

class SemiblockValueReporter<T>(
    var value: T? = null
) {

    fun obtainValueAndRelease(default: T): T {
        val current = value ?: return default

        return current.also {
            value = null
        }
    }
}