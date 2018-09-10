package com.isidroid.a18.sample

import android.view.View
import com.isidroid.a18.R
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.a18.sample.viewmodel.EmployeesViewModel
import com.isidroid.utilsmodule.adapters.CoreBindAdapter
import com.isidroid.utilsmodule.adapters.CoreHolder
import com.isidroid.utilsmodule.adapters.CoreLoadingHolder

class EmployeeAdapter : CoreBindAdapter<Employee, SampleItemEmployeeBinding>() {
    override val loadingResource = R.layout.sample_item_employee_loading
    override val hasInitialLoading = true
    lateinit var viewModel: EmployeesViewModel

    override fun resource(viewType: Int): Int {
        return R.layout.sample_item_employee
    }

    override fun createHolder(binding: SampleItemEmployeeBinding, viewType: Int): CoreHolder {
        return EmployeeHolder(binding).apply { binding.viewModel = viewModel }
    }

    override fun createLoadingHolder(view: View): CoreLoadingHolder {
        return EmployeeLoadingHolder(view)
    }
}