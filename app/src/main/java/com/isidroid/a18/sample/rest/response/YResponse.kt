package com.isidroid.a18.sample.rest.response

import com.google.gson.annotations.SerializedName

open class YResponse<T>(
        @SerializedName("data") var data: T? = null,
        @SerializedName("kind") var kind: String
)

data class ListingResponse<T>(
        @SerializedName("after") var after: String? = null,
        @SerializedName("before") var before: String? = null,
        @SerializedName("limit") var limit: Int,
        @SerializedName("count") var count: Int,
        @SerializedName("children") var children: List<T>
)