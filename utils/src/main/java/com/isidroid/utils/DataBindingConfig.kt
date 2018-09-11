package com.isidroid.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageurl")
fun loadImage(imageView: ImageView, url: String?) {
    DataBindingConfig.instance.imageLoader?.invoke(imageView, url)
}

open class DataBindingConfig private constructor() {
    internal var imageLoader: ((imageView: ImageView, url: String?) -> Unit)? = null

    fun withImageLoader(callback: (imageView: ImageView, url: String?) -> Unit) = apply {
        this.imageLoader = callback
    }

    companion object {
        internal val instance: DataBindingConfig by lazy { DataBindingConfig() }
        fun create(): DataBindingConfig = instance
    }
}