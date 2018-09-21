package com.isidroid.a18.sample.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
open class Api<T>(private val cl: Class<T>, private val endPoint: String) {
    enum class LogLevel {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    protected var debug = true
    protected var okhttpClientDecorator: ((builder: OkHttpClient.Builder) -> Unit)? = null
    protected var offlineMaxStale: Int = 1
    protected var maxStaleUnit = TimeUnit.DAYS
    protected var timeout = 15L
    protected var gson: Gson = GsonBuilder().create()
    protected var logLevel = LogLevel.BASIC

    fun withDebug(isEnabled: Boolean) = apply { this.debug = isEnabled }
    fun withDecorator(decorator: (builder: OkHttpClient.Builder) -> Unit) = apply { this.okhttpClientDecorator = decorator }
    fun withOfflineMaxStale(offlineMaxStale: Int) = apply { this.offlineMaxStale = offlineMaxStale }
    fun withMaxStaleUnit(maxStaleUnit: TimeUnit) = apply { this.maxStaleUnit = maxStaleUnit }
    fun withTimeout(timeout: Long) = apply { this.timeout = timeout }
    fun withGson(gson: Gson) = apply { this.gson = gson }
    fun withLogLevel(level: LogLevel) = apply { this.logLevel = level }

    fun <T> build(): T {
        val logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag(cl.simpleName).i(message)
            }
        }

        val builder = OkHttpClient().newBuilder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor(logger).withLevel(logLevel))

        okhttpClientDecorator?.invoke(builder)

        val retrofit = Retrofit.Builder()
                .baseUrl(endPoint)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(cl) as T
    }
}