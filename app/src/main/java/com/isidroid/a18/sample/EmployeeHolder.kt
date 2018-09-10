package com.isidroid.a18.sample

import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.utilsmodule.adapters.CoreBindHolder
import timber.log.Timber

class EmployeeHolder(b: SampleItemEmployeeBinding) : CoreBindHolder<Employee, SampleItemEmployeeBinding>(b) {
    override fun onBind(item: Employee) {
        binding.employee = item
        binding.imageView.setOnClickListener {
            item.image = "https://cdn.shopify.com/s/files/1/0659/4707/products/PMW222.jpg?v=1527388268"
            binding.viewModel?.edit(item)
        }

        binding.textView.setOnClickListener { binding.viewModel?.remove(item) }
    }
}