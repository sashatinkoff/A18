package com.isidroid.a18

import com.bumptech.glide.Glide
import com.isidroid.utils.BindAdapterHelper

object BindAdapter {
    fun create() {
        BindAdapterHelper.instance.withImageLoader { imageView, url ->
            Glide.with(imageView).load(url).into(imageView)
        }
    }
}