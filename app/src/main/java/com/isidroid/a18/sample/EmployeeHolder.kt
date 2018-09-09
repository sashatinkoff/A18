package com.isidroid.a18.sample

import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.utilsmodule.adapters.CoreBindHolder

class EmployeeHolder(b: SampleItemEmployeeBinding) : CoreBindHolder<Employee, SampleItemEmployeeBinding>(b) {
    override fun onBind(item: Employee) {
        binding.employee = item
    }
}