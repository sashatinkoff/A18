package com.isidroid.a18.sample.rest

import com.google.gson.annotations.SerializedName
import com.isidroid.a18.rest.Api
import io.reactivex.Flowable
import retrofit2.http.GET

private const val ENDPOINT = "https://jsonplaceholder.typicode.com/"

interface ApiTest {

    @GET("posts")
    fun posts(): Flowable<List<PostResponse>>


    data class PostResponse(@SerializedName("id") var id: Int, @SerializedName("userId") var userId: Int)
    companion object {
        fun create(): ApiTest = Api(ApiTest::class.java, ENDPOINT).build()
    }
}