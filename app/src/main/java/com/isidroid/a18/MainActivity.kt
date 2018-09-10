package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.a18.sample.Employee
import com.isidroid.a18.sample.EmployeeAdapter
import com.isidroid.a18.sample.viewmodel.EmployeesViewModel
import com.isidroid.utilsmodule.BaseActivity
import com.isidroid.utilsmodule.adapters.CoreBindAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var adapter: CoreBindAdapter<Employee, SampleItemEmployeeBinding>

    private lateinit var viewModel: EmployeesViewModel

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = EmployeeAdapter()
                .onLoadMore { more() }

        viewModel = ViewModelProviders.of(this).get(EmployeesViewModel::class.java)
        viewModel.data.observe(this, Observer {
            swipeLayout.isRefreshing = false
            adapter.update(it, true)
        })


        materialButton.setOnClickListener {
//            val item = adapter.items.first()//Employee(122, "", 10) //
//            item.email = "Sasha"
//            adapter.update(item)

            finish()
        }

        swipeLayout.isRefreshing = true

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        adapter.loadMore()

        swipeLayout.setOnRefreshListener { adapter.reset() }
    }

    private fun more() {
        viewModel.loadMore()
    }
}

