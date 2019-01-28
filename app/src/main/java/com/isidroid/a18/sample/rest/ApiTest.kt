package com.isidroid.a18.sample.rest

import com.isidroid.a18.rest.Api
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET

private const val ENDPOINT = "https://jsonplaceholder.typicode.com/"

interface ApiTest {

    @GET("posts")
    fun posts(): Flowable<ResponseBody>

    companion object {
        fun create(): ApiTest = Api(ApiTest::class.java, ENDPOINT).build()
    }
}