package com.isidroid.a18.sample.rest

import com.google.gson.annotations.SerializedName
import com.isidroid.a18.App
import com.isidroid.a18.rest.Api
import retrofit2.Call
import retrofit2.http.GET

private const val ENDPOINT = "https://jsonplaceholder.typicode.comsdfsdfsdfsdf/"

interface ApiTest {

    @GET("posts")
    fun posts(): Call<List<PostResponse>>


    data class PostResponse(@SerializedName("id") var id: Int, @SerializedName("userId") var userId: Int)
    companion object {
        fun create(): ApiTest =
            Api(cl = ApiTest::class.java, endPoint = ENDPOINT, context = App.instance).build()
    }
}