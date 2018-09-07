package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.sample.Employee
import com.isidroid.a18.sample.EmployeeAdapter
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val employee = Employee(10, "Sasha")
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.employee = employee

        val adapter = EmployeeAdapter()
        (0 until 10).forEach {
            adapter.add(Employee(adapter.itemCount + 1, UUID.randomUUID().toString().substring(0, 5)))
        }


        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        materialButton.setOnClickListener {

        }
    }
}

