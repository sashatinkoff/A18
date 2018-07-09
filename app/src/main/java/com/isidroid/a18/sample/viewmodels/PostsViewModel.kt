package com.isidroid.a18.sample.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class PostsViewModel(private val repository: IPostsRepository, private val compositeDisposable: CompositeDisposable) : ViewModel() {

    val data by lazy {
        repository.data.toLiveData(compositeDisposable)
    }

    fun posts() {
        repository.posts()
    }
}