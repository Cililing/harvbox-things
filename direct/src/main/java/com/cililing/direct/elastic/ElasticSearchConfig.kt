package com.cililing.direct.elastic

object ElasticSearchConfig {
    const val url: String = "https://scalr.api.appbase.io"
    const val appName: String = "harvbox"

    private const val credentials: String = "credentials..."
    val username = credentials.split(":")[0]
    val password = credentials.split(":")[1]

    val reportCooldown = 60_000 // 60 seconds for development... Final will be about 10 min (600_000).
}