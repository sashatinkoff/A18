package com.isidroid.a18.sample.viewmodels

import android.annotation.SuppressLint
import com.isidroid.a18.core.Outcome
import com.isidroid.a18.core.failed
import com.isidroid.a18.core.loading
import com.isidroid.a18.core.success
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class PostsRepository(private val compositeDisposable: CompositeDisposable) : IPostsRepository {
    override val data = PublishSubject.create<Outcome<List<Post>>>()

    @SuppressLint("CheckResult")
    override fun posts() {
        data.loading(true)

        val disposable = ApiService.Factory.create().posts()
                .doOnNext { Thread.sleep(500) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data.success(it) },
                        { data.failed(it) }
                )

        compositeDisposable.add(disposable)
    }
}