package com.isidroid.a18.rest

import com.google.gson.GsonBuilder
import com.isidroid.a18.rest.interceptors.AuthInterceptor

object ApiFactory {
    const val ENDPOINT = "http://dev.teamprinter.com/api/v1/"
    const val TIMEOUT = 15L

    private fun <T> createBase(cl: Class<T>): Api<T> {
        return Api(cl, ENDPOINT)
                .withLogLevel(Api.LogLevel.BODY)
                .withDebug(true)
                .withGson(GsonBuilder().create())
                .withDecorator {
                    it.addInterceptor(AuthInterceptor())
                }
                .withTimeout(TIMEOUT)
    }


    fun <T> create(cl: Class<T>, level: Api.LogLevel? = null): T {
        return createBase(cl).withLogLevel(level).build()
    }
}