package com.isidroid.a18.sample

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import timber.log.Timber

@BindingAdapter("imageurl")
fun loadImage(view: ImageView, url: String){
    Glide.with(view).load(url).into(view)
}


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