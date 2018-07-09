package com.isidroid.a18.sample.viewmodels.di

object PostsDH {
    val posts by lazy { DaggerPostsComponent.create() }
}