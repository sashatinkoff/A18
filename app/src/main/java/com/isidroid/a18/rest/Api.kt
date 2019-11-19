package com.isidroid.a18.rest

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isidroid.utils.extensions.isConnected
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class Api<T>(
    private val cl: Class<T>,
    private val endPoint: String,
    private val timeout: Long = 15L,
    private val context: Context? = null,
    private val clientBuilder: ((OkHttpClient.Builder) -> Unit)? = null,
    private val logLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
) {
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .create()

    fun <T> build(): T {
        val logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag(cl.simpleName).i(message)
            }
        }

        val loggerInterceptor = HttpLoggingInterceptor(logger)
        loggerInterceptor.level = logLevel

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept-Encoding", "")
                .build()

            chain.proceed(request)
        }

        val noInternetConnectioInterceptor = Interceptor { chain ->
            if (!context.isConnected()) throw NoInternetConnection()
            chain.proceed(chain.request())
        }

        val builder = OkHttpClient().newBuilder()
            .readTimeout(timeout, TimeUnit.SECONDS)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(noInternetConnectioInterceptor)
            .addInterceptor(authInterceptor)

            .addNetworkInterceptor(loggerInterceptor)

        clientBuilder?.invoke(builder)

        val retrofit = Retrofit.Builder()
            .baseUrl(endPoint)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(cl) as T
    }
}

class NoInternetConnection : Throwable()