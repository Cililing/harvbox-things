package com.cililing.direct.elastic

import com.google.gson.Gson
import org.koin.dsl.module

fun getElasticModule() = module {

    single {
        Gson() as Gson
    }

    single {
        ElasticSearchImpl(
                get(),
                get(), // logger
                get(), // clock
                ElasticSearchConfig
        ) as ElasticSearch
    }
}