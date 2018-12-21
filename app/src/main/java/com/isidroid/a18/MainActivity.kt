package com.isidroid.a18

import android.os.Bundle
import com.isidroid.realm.realm
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.subscribeIoMain
import io.reactivex.Flowable
import timber.log.Timber


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}