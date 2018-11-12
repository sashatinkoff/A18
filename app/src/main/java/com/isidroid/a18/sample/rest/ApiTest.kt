package com.isidroid.a18.sample.rest

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiTest {

    @GET("posts")
    fun posts(): Flowable<ResponseBody>

    object Factory {
        fun create(): ApiTest {
            val endPoint = "https://jsonplaceholder.typicode.com/"
            return Api(ApiTest::class.java, endPoint)
                    .build()
        }
    }
}