package com.isidroid.a18.sample.viewmodels

import androidx.lifecycle.MediatorLiveData

interface IPostsRepository {
    val data: MediatorLiveData<Outcome<List<Post>>>

    fun posts()
}