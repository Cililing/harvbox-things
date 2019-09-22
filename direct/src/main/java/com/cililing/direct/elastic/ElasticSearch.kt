package com.cililing.direct.elastic

import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.toJson
import com.google.gson.Gson
import io.appbase.client.AppbaseClient
import java.lang.Exception

internal interface ElasticSearch {
    suspend fun reportSnapshot(snapshot: StatusSnapshot)
}

internal class ElasticSearchImpl(
        private val gson: Gson,
        private val logger: Logger,
        private val clock: Clock,
        elasticSearchConfig: ElasticSearchConfig = ElasticSearchConfig
): ElasticSearch {
    private val elasticClient = AppbaseClient(
            elasticSearchConfig.url,
            elasticSearchConfig.appName,
            elasticSearchConfig.username,
            elasticSearchConfig.password
    )

    private val cooldown = elasticSearchConfig.reportCooldown
    private var lastReport: Long? = null

    private fun shouldReport(): Boolean {
        return (lastReport?.plus(cooldown) ?: Long.MIN_VALUE) > clock.micro()
    }

    override suspend fun reportSnapshot(snapshot: StatusSnapshot) {
        if (shouldReport().not()) {
            return
        }

        try {
            elasticClient.prepareIndex("_doc", snapshot.toJson(gson))
                    .execute()
                    .also {
                        logger.i("ElasticResponse: $it")
                    }
        } catch (ex: Exception) {
            logger.e("ElasticError", exception = ex)
        }
    }
}