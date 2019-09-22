package com.cililing.direct.elastic

import org.koin.dsl.module

fun getElasticModule() = module {
    single {
        ElasticSearchImpl(
                get(),
                get(), // logger
                get(), // clock
                ElasticSearchConfig
        ) as ElasticSearch
    }
}