package com.isidroid.utilsmodule.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class CoreBindAdapter<T, B : ViewDataBinding, VH : CoreBindHolder<T, B>> : RecyclerView.Adapter<VH>() {
    val items = mutableListOf<T>()

    fun add(vararg items: T) = apply { this.items.addAll(items) }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding: B = DataBindingUtil.inflate(LayoutInflater.from(parent.context), resource(viewType), parent, false)
        return createHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    fun update(item: T) {
        val position = items.indexOf(item)
        if (position >= 0) {
            onUpdate(item)
            notifyItemChanged(position)
        }
    }

    // Open and abstract functions
    open fun onUpdate(item: T) {}

    abstract fun resource(viewType: Int): Int
    abstract fun createHolder(binding: B, viewType: Int): VH
}