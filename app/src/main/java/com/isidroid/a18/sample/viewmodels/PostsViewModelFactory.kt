package com.isidroid.a18.sample.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable

@Suppress("UNCHECKED_CAST")
class PostsViewModelFactory(val repository: IPostsRepository, val compositeDisposable: CompositeDisposable) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostsViewModel(repository, compositeDisposable) as T
    }
}