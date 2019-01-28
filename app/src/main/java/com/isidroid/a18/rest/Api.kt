package com.isidroid.a18.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isidroid.a18.rest.interceptors.HttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class Api<T>(private val cl: Class<T>, private val endPoint: String) {
    enum class LogLevel {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    private var debug = true
    private var okhttpClientDecorator: ((builder: OkHttpClient.Builder) -> Unit)? = null
    private var offlineMaxStale: Int = 1
    private var maxStaleUnit = TimeUnit.DAYS
    private var timeout = 15L
    private var gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()
    private var logLevel = LogLevel.BASIC

    fun withDebug(isEnabled: Boolean) = apply { this.debug = isEnabled }
    fun withDecorator(decorator: (builder: OkHttpClient.Builder) -> Unit) = apply { this.okhttpClientDecorator = decorator }
    fun withOfflineMaxStale(offlineMaxStale: Int) = apply { this.offlineMaxStale = offlineMaxStale }
    fun withMaxStaleUnit(maxStaleUnit: TimeUnit) = apply { this.maxStaleUnit = maxStaleUnit }
    fun withTimeout(timeout: Long) = apply { this.timeout = timeout }
    fun withGson(gson: Gson) = apply { this.gson = gson }
    fun withLogLevel(level: LogLevel?) = apply { this.logLevel = level ?: LogLevel.BASIC }

    fun <T> build(): T {
        val loggerInterceptor = HttpLoggingInterceptor(object :
            HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag(cl.simpleName).i(message)
            }
        }).withLevel(logLevel)

        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept-Encoding", "")
                .build()

            chain.proceed(request)
        }

        val builder = OkHttpClient().newBuilder()
            .readTimeout(timeout, TimeUnit.SECONDS)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(loggerInterceptor)

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