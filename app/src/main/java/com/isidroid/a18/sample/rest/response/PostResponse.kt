package com.isidroid.a18.sample.rest.response

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("id") var id: String,
    @SerializedName("title") var title: String
)

data class PostDataResponse(@SerializedName("data") var data: PostResponse)