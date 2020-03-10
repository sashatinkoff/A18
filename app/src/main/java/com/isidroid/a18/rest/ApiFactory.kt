package com.isidroid.a18.rest

import okhttp3.logging.HttpLoggingInterceptor

object ApiFactory {
    const val ENDPOINT = "https://ya.ru/"
    const val TIMEOUT = 15L

    fun <T> create(
        endpoint: String = ENDPOINT,
        cl: Class<T>,
        level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
    ): T {
        return Api(
            cl = cl,
            endPoint = endpoint,
            logLevel = level,
            timeout = TIMEOUT
        ).build()
    }
}