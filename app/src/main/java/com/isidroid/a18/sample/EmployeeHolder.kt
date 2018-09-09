package com.isidroid.a18.sample

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.utilsmodule.adapters.CoreBindHolder

@BindingAdapter("imageurl")
fun loadImage(view: ImageView, url: String) {
    Glide.with(view).load(url).into(view)
}

class EmployeeHolder(b: SampleItemEmployeeBinding) : CoreBindHolder<Employee, SampleItemEmployeeBinding>(b) {
    override fun onBind(item: Employee) {
        binding.employee = item
    }
}