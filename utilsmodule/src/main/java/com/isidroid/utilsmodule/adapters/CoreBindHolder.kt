package com.isidroid.utilsmodule.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class CoreBindHolder<T, B : ViewDataBinding>(protected val binding: B) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: T) {
        onBind(item)
        binding.executePendingBindings()
    }

    abstract fun onBind(item: T)
}