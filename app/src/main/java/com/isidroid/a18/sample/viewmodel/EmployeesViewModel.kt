package com.isidroid.a18.sample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.sample.Employee
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

const val ACTION_EDIT = "edit"
const val ACTION_REMOVE = "remove"

class EmployeesViewModel : ViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private var repository = EmployeesRepository(compositeDisposable)

    var employees = MutableLiveData<MutableList<Employee>>()
    var editEmployee = MutableLiveData<EmployeeAction>()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun loadMore() {
        repository.load { employees.postValue(it) }
    }

    fun edit(item: Employee) {
        repository.edit(item) { editEmployee.postValue(it) }
    }

    fun remove(item: Employee) {
        repository.remove(item) {
            employees.value?.remove(it.employee)
            editEmployee.postValue(it)
        }
    }
}

class EmployeeAction(val employee: Employee, val action: String) {
    val isEdit: Boolean by lazy { action == ACTION_EDIT }
    val isRemove: Boolean by lazy { action == ACTION_REMOVE }
}