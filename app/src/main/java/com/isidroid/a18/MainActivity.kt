package com.isidroid.a18

import android.os.Bundle
import com.isidroid.utilsmodule.BaseActivity
import timber.log.Timber


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("onCreate ${intent.action}")

        intent?.extras?.keySet()?.forEach {
            Timber.i("$it=${intent?.extras?.get(it)}")
        }
    }

}
