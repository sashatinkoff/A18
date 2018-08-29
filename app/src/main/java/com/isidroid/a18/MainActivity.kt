package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection


class MainActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, DetailFragment.create(), "notags")
            commit()
        }
    }
}

