package com.cililing.direct.elastic

import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.toValueJson
import com.google.gson.Gson
import io.appbase.client.AppbaseClient

internal interface ElasticSearch {
    suspend fun reportSnapshot(snapshot: StatusSnapshot)
    fun setCooldown(cooldown: Long)
}

internal class ElasticSearchImpl(
    private val gson: Gson,
    private var cooldown: Long,
    private val logger: Logger,
    private val clock: Clock,
    private val elasticSearchConfig: ElasticSearchConfig = ElasticSearchConfig
) : ElasticSearch {
    private val elasticClient = AppbaseClient(
            elasticSearchConfig.url,
            elasticSearchConfig.appName,
            elasticSearchConfig.username,
            elasticSearchConfig.password
    )

    private var nextPossibleReportTime: Long? = null

    override fun setCooldown(cooldown: Long) {
        this.cooldown = cooldown
    }

    private fun shouldReport(): Boolean {
        return elasticSearchConfig.isEnabled &&
                clock.milis() > nextPossibleReportTime ?: Long.MIN_VALUE
    }

    override suspend fun reportSnapshot(snapshot: StatusSnapshot) {
        if (!shouldReport()) {
            logger.i("ElasticSearch ${clock.milis()}---cool_down---$nextPossibleReportTime")
            return
        }

        try {
            val response = elasticClient.prepareIndex("_doc", snapshot.toValueJson(gson))
                    .execute()
            nextPossibleReportTime = clock.milis() + cooldown
            logger.i("ElasticResponse succeed. NextReportTime: $nextPossibleReportTime" +
                    "\n\tresponse: $response")
        } catch (ex: Exception) {
            logger.e("ElasticError", exception = ex)
        }
    }
}