package com.cililing.direct.elastic

import com.cililing.direct.BuildConfig

object ElasticSearchConfig {
    const val isEnabled = true
    const val url: String = "https://scalr.api.appbase.io"
    const val appName: String = "harvbox"

    private const val credentials: String = BuildConfig.ELASTIC_SEARCH_ADMIN_KEY
    val username = credentials.split(":")[0]
    val password = credentials.split(":")[1]

    const val reportCooldownMilis = 15_000 // 15 seconds for development... Final will be about 10 min (600_000).
}