package com.cililing.direct.elastic

import org.koin.dsl.module

fun getElasticModule(cooldown: Long) = module {
    single {
        ElasticSearchImpl(
                get(),
                cooldown,
                get(), // logger
                get(), // clock
                ElasticSearchConfig
        ) as ElasticSearch
    }
}