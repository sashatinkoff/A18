package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.a18.sample.Employee
import com.isidroid.a18.sample.EmployeeAdapter
import com.isidroid.utilsmodule.BaseActivity
import com.isidroid.utilsmodule.adapters.CoreBindAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {
    private lateinit var adapter: CoreBindAdapter<Employee, SampleItemEmployeeBinding>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = EmployeeAdapter()
                .onLoadMore { more() }


        materialButton.setOnClickListener {
            val item = adapter.items.first()//Employee(122, "", 10) //
            item.email = "Sasha"
            adapter.update(item)
        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        adapter.loadMore()
    }

    private fun more() {
        Timber.tag("Diagnostic").i("Activity.Load more")

        val result = mutableListOf<Employee>()
        (0 until 4).forEach { pos ->
            val email = "${adapter.items.size + pos}@gmail.com"
            result.add(Employee(email))
        }

        adapter.update(result, true)
    }
}

