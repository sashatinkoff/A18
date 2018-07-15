package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection


class MainActivity : BaseActivity() {
    var items = mutableListOf<Int>()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add(arrayListOf(20,201,101))

//        add(arrayListOf(1, 2, 3)) {}
//        Timber.i("1. $items")
//
//        add(arrayListOf(8, 9, 10)) { it.sortDescending()}
//        Timber.i("2. $items")
    }

    fun add(i: List<Int>, callback: ((items: MutableList<Int>) -> Unit)? = null) {
        items.addAll(i)
        callback?.invoke(items)
    }
}
