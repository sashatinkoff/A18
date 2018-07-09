package com.isidroid.a18.sample.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class PostsViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository: IPostsRepository = PostsRepository(compositeDisposable)

    val data by lazy {
        repository.data.toLiveData(compositeDisposable)
    }

    fun posts() {
        repository.posts()
    }
}