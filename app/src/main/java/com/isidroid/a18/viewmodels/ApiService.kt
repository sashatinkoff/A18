package com.isidroid.a18.viewmodels

import com.google.gson.Gson
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val URL = "https://jsonplaceholder.typicode.com/"

interface ApiService {
    @GET("posts")
    fun posts(): Flowable<List<Post>>

    object Factory {
        fun create(): ApiService {
            return Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(OkHttpClient())
                    .build()
                    .create(ApiService::class.java)
        }
    }
}