package com.isidroid.a18

import android.os.Bundle
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.utils.UpgradeHelper
import timber.log.Timber


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Timber.i("upgrader=${UpgradeHelper.get()}")
    }
}