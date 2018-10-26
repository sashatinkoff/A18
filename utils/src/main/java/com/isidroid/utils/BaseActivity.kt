package com.isidroid.utils

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.isidroid.utils.utils.views.YViewUtils

abstract class BaseActivity : AppCompatActivity(), LifecycleObserver {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
        onCreateBinding()
        onCreateViewModel()
    }

    override fun onPause() {
        super.onPause()
        YViewUtils.hideSoftKeyboard(this)
    }

    abstract fun onCreateBinding()
    abstract fun onCreateViewModel()
}