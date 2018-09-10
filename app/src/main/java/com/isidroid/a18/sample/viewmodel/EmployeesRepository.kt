package com.isidroid.a18.sample.viewmodel

import com.isidroid.a18.sample.Employee
import com.isidroid.utilsmodule.addTo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EmployeesRepository(private val compositeDisposable: CompositeDisposable) {
    fun load(callback: ((List<Employee>) -> Unit)) {
        Flowable.just(5)
                .map {
                    Thread.sleep(3000)
                    val result = mutableListOf<Employee>()
                    (0 until it).forEach { result.add(Employee("$it@")) }
                    result
                }

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { callback.invoke(it) }
                .addTo(compositeDisposable)
    }
}