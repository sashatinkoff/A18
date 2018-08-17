package com.isidroid.a18

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import com.isidroid.loggermodule.Diagnostics.Companion.LOGTAG
import com.isidroid.loggermodule.diagnostics
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection
import timber.log.Timber


class MainActivity : BaseActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}

