package com.isidroid.a18.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Api<T>(
    private val cl: Class<T>,
    private val endPoint: String,
    private val logLevel: HttpLoggingInterceptor.Level,
    private val okhttpClientDecorator: ((builder: OkHttpClient.Builder) -> Unit)? = null,
    private val timeout: Long = 15L,
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .setLenient()
        .create()
) {

    @Suppress("UNCHECKED_CAST")
    fun <T> build(): T {
        val loggerInterceptor = HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Timber.tag(cl.simpleName).i(message)
                }
            }).apply { level = logLevel }

        val builder = OkHttpClient().newBuilder()
            .readTimeout(timeout, TimeUnit.SECONDS)
            .connectTimeout(timeout, TimeUnit.SECONDS)

        builder.addNetworkInterceptor(loggerInterceptor)
        okhttpClientDecorator?.invoke(builder)

        val retrofit = Retrofit.Builder()
            .baseUrl(endPoint)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(cl) as T
    }
}