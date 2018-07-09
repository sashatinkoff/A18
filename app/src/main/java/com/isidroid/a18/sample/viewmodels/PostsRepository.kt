package com.isidroid.a18.sample.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.MediatorLiveData
import io.reactivex.schedulers.Schedulers

class PostsRepository : IPostsRepository {
    override val data: MediatorLiveData<Outcome<List<Post>>> = MediatorLiveData()

    @SuppressLint("CheckResult")
    override fun posts() {
        data.postValue(Outcome.loading(true))

        val disposable = ApiService.Factory.create().posts()
                .doOnNext { Thread.sleep(1000) }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            data.postValue(Outcome.loading(false))
                            data.postValue(Outcome.success(it))
                        },
                        {
                            data.postValue(Outcome.loading(false))
                            data.postValue(Outcome.failure(it))
                        }
                )
    }
}