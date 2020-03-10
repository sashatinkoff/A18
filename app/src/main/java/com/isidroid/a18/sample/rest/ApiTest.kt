package com.isidroid.a18.sample.rest

import com.google.gson.annotations.SerializedName
import com.isidroid.a18.rest.ApiFactory
import retrofit2.Call
import retrofit2.http.GET

private const val ENDPOINT = "https://jsonplaceholder.typicode.com/"

interface ApiTest {

    @GET("posts")
    fun posts(): Call<List<PostResponse>>


    data class PostResponse(
        @SerializedName("id") var id: Int,
        @SerializedName("userId") var userId: Int
    )

    companion object {
        fun create(): ApiTest = ApiFactory.create(ENDPOINT, cl = ApiTest::class.java)
    }
}