package com.isidroid.a18.sample

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageurl")
fun loadImage(view: ImageView, url: String?) {
    url?.let { Glide.with(view).load(url).into(view) }
}