package com.isidroid.a18.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isidroid.a18.R
import com.isidroid.a18.databinding.SampleItemEmployeeBinding

class EmployeeAdapter : RecyclerView.Adapter<EmployeeHolder>() {
    val items = mutableListOf<Employee>()

    fun add(vararg employee: Employee) = apply { items.addAll(employee) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {
        val binding: SampleItemEmployeeBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.sample_item_employee, parent, false)
        return EmployeeHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {
        holder.bind(items[position])
    }

}