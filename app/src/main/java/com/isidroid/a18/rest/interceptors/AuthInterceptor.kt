package com.isidroid.a18.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val HEADER_AUTH = "Authorization"

class AuthInterceptor : Interceptor {
    private fun token(): String {
        return "" //SessionManager.get().session?.token ?: ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader(HEADER_AUTH, token())

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        val modifiedResponse = when (response.code()) {
            401 -> handle401(chain, request)
            else -> response
        }

        return modifiedResponse ?: response
    }


    private fun handle401(chain: Interceptor.Chain, request: Request): Response? {
//        val refreshResponse = ApiSession.create().refresh().toFuture().get()
//        return if (refreshResponse.isSuccessful) {
//            SessionManager.get().session?.saveToken(refreshResponse.body()?.data?.token)
//
//            val builder = request.newBuilder()
//                .removeHeader(HEADER_AUTH)
//                .addHeader(HEADER_AUTH, token())
//
//            chain.proceed(builder.build())
//        } else null
        return null
    }
}