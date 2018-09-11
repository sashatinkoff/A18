package com.isidroid.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.isidroid.utils.utils.views.YViewUtils

abstract class BaseActivity : AppCompatActivity(), LifecycleObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
    }

    override fun onPause() {
        super.onPause()
        YViewUtils.hideSoftKeyboard(this)
    }
}