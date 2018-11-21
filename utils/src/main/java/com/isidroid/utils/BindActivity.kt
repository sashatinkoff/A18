package com.isidroid.utils

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindActivity<D : ViewDataBinding> : BaseActivity() {
    protected lateinit var binding: D
    abstract val resource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, resource)
        onCreateBinding()
    }

    open fun onCreateBinding() {}
}