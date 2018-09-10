package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.databinding.SampleItemEmployeeBinding
import com.isidroid.a18.sample.Employee
import com.isidroid.a18.sample.EmployeeAdapter
import com.isidroid.a18.sample.viewmodel.EmployeesViewModel
import com.isidroid.utilsmodule.BaseActivity
import com.isidroid.utilsmodule.adapters.CoreBindAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {
    private lateinit var adapter: CoreBindAdapter<Employee, SampleItemEmployeeBinding>
    private lateinit var viewModel: EmployeesViewModel
    private var counter = 0

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(EmployeesViewModel::class.java)

        adapter = EmployeeAdapter().apply { this.viewModel = this@MainActivity.viewModel }
                .create()
                .onLoadMore { more() }

        viewModel.employees.observe(this, Observer {
            val hasMore = adapter.items.size == 0

            swipeLayout.isRefreshing = false
            adapter.insert(it, hasMore)
        })

        viewModel.editEmployee.observe(this, Observer {
            swipeLayout.isRefreshing = false
            if (it.isEdit) adapter.update(it.employee)
            else if (it.isRemove) adapter.remove(it.employee)
        })


        materialButton.setOnClickListener {
            val item = adapter.items.first()//Employee(122, "", 10) //
            item.email = "Sasha"
            swipeLayout.isRefreshing = true

            when (counter) {
                0 -> viewModel.edit(item)
                1 -> viewModel.remove(item)
            }
            counter++
        }

        (recyclerview.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        swipeLayout.setOnRefreshListener {
            counter = 0
            adapter.reset()
        }
    }

    private fun more() {
        viewModel.loadMore()
    }
}

