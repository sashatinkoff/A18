package com.isidroid.a18.sample

import com.isidroid.a18.R
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.utilsmodule.adapters.CoreBindAdapter
import com.isidroid.utilsmodule.adapters.CoreHolder

class EmployeeAdapter : CoreBindAdapter<Employee, SampleItemEmployeeBinding>() {

    override fun resource(viewType: Int): Int {
        return R.layout.sample_item_employee
    }

    override fun createHolder(binding: SampleItemEmployeeBinding, viewType: Int): CoreHolder {
        return EmployeeHolder(binding)
    }

}