package com.isidroid.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageurl")
fun loadImage(imageView: ImageView, url: String?) {
    BindAdapterHelper.instance.imageLoader?.invoke(imageView, url)
}

class BindAdapterHelper private constructor() {
    internal var imageLoader: ((imageView: ImageView, url: String?) -> Unit)? = null

    fun withImageLoader(callback: (imageView: ImageView, url: String?) -> Unit) = apply {
        this.imageLoader = callback
    }

    companion object {
        val instance: BindAdapterHelper by lazy { BindAdapterHelper() }
    }
}