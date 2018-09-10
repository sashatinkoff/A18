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
import com.isidroid.utilsmodule.addTo
import dagger.android.AndroidInjection
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


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
        Flowable.just(5)
                .map {
                    Thread.sleep(1000)
                    val result = mutableListOf<Employee>()
                    (0 until it).forEach { pos ->
                        val email = "${adapter.items.size + pos}@gmail.com"
                        result.add(Employee(email))
                    }
                    result
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adapter.update(it, true) }
                .addTo(CompositeDisposable())
    }
}

