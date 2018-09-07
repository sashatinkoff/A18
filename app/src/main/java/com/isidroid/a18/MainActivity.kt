package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.sample.adapters.Employee
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection


class MainActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val employee = Employee(10, "Sasha")
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.employee = employee

    }
}

