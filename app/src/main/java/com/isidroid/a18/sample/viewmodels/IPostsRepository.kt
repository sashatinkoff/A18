package com.isidroid.a18.sample.viewmodels

import com.isidroid.a18.core.Outcome
import io.reactivex.subjects.PublishSubject

interface IPostsRepository {
    val data: PublishSubject<Outcome<List<Post>>>
    fun posts()
}