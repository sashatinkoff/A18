package com.isidroid.a18.sample

import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.utilsmodule.adapters.CoreBindHolder
import timber.log.Timber

class EmployeeHolder(b: SampleItemEmployeeBinding) : CoreBindHolder<Employee, SampleItemEmployeeBinding>(b) {
    override fun onBind(item: Employee) {
        binding.employee = item
        binding.imageView.setOnClickListener {
            item.age++
            binding.viewModel?.edit(item)
        }
    }
}