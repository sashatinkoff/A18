package com.isidroid.a18.sample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isidroid.a18.sample.Employee
import io.reactivex.disposables.CompositeDisposable

class EmployeesViewModel : ViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private var repository = EmployeesRepository(compositeDisposable)

    var data = MutableLiveData<List<Employee>>()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadMore() {
        repository.load { data.postValue(it) }
    }
}