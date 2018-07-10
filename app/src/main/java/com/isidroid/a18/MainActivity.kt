package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import com.isidroid.utilsmodule.BaseActivity
import com.isidroid.utilsmodule.addTo
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber


class MainActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val compositeDisposable = CompositeDisposable()
        var subject = PublishSubject.create<Int>()
        subject.subscribe { Timber.i("1next 1 $it") }.addTo(compositeDisposable)

        subject.onNext(1)
        subject.onNext(2)
        compositeDisposable.clear()
        subject.subscribe { Timber.i("2next $it") }.addTo(compositeDisposable)

        subject.onNext(3)
    }

}
