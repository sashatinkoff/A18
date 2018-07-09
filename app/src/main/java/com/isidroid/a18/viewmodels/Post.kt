package com.isidroid.a18.viewmodels

import com.google.gson.annotations.SerializedName

class Post {
    @SerializedName("userId") var userId = 0
    @SerializedName("id") var id = 0
    @SerializedName("title") var title = ""
    @SerializedName("body") var body = ""
}
