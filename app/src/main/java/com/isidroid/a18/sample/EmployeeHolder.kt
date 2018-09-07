package com.isidroid.a18.sample

import androidx.recyclerview.widget.RecyclerView
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import timber.log.Timber

class EmployeeHolder(val binding: SampleItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root), OnEmployeeClickListener {
    fun bind(employee: Employee) {
        binding.employee = employee
        binding.onclicklistener = this
        binding.executePendingBindings()
    }

    override fun onClick(employee: Employee) {
        Timber.i("onClick $employee")
    }
}

interface OnEmployeeClickListener {
    fun onClick(employee: Employee)
}