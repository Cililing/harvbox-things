package com.cililing.direct.firebase

import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.TAG
import java.lang.Exception

internal interface FirebaseFailureHandler {
    fun handle(ex: Exception)
}

internal class FirebaseFailureHandlerImpl(
    private val logger: Logger
) : FirebaseFailureHandler {
    override fun handle(ex: Exception) {
        logger.e(this.TAG, exception = ex)
    }
}