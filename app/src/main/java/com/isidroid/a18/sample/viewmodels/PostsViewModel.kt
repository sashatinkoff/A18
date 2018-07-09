package com.isidroid.a18.sample.viewmodels

import androidx.lifecycle.ViewModel

class PostsViewModel : ViewModel() {
    private val repository: IPostsRepository = PostsRepository()
    val data by lazy {
        repository.data
    }

    fun posts() {
        repository.posts()
    }
}