package com.isidroid.utils.adapters

import androidx.databinding.ViewDataBinding

abstract class CoreBindHolder<T, B : ViewDataBinding>(val binding: B) : CoreHolder(binding.root) {
    fun bind(item: T) {
        onBind(item)
        binding.executePendingBindings()
    }

    abstract fun onBind(item: T)
}