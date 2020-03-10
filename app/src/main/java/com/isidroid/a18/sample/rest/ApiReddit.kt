package com.isidroid.a18.sample.rest

import com.isidroid.a18.rest.ApiFactory
import com.isidroid.a18.sample.rest.response.ListingResponse
import com.isidroid.a18.sample.rest.response.PostDataResponse
import com.isidroid.a18.sample.rest.response.YResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiReddit {

    @GET("r/{subreddit}/.json")
    fun posts(@Path("subreddit") subreddit: String): Call<YResponse<ListingResponse<PostDataResponse>>>

    companion object {
        fun create() = ApiFactory.create(
            cl = ApiReddit::class.java,
            endpoint = "https://www.reddit.com/"
        )
    }
}