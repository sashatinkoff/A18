package com.isidroid.utilsmodule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.isidroid.utilsmodule.R

abstract class CoreBindAdapter<T, B : ViewDataBinding, VH : CoreBindHolder<T, B>> : RecyclerView.Adapter<VH>() {
    val items = mutableListOf<T>()
    open var hasMore = false

    fun add(vararg items: T) = apply { this.items.addAll(items) }

    override fun getItemCount(): Int {
        var size = items.size
        if (hasMore) size++
        return size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size && hasMore) VIEW_TYPE_LOADING
        else VIEW_TYPE_NORMAL
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            VIEW_TYPE_LOADING -> CoreLoadingHolder(loadingView(parent)) as VH
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: B = DataBindingUtil.inflate(inflater, resource(viewType), parent, false)
                createHolder(binding, viewType)
            }
        }

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        (holder as? VH)?.bind(items[position])
    }

    fun update(item: T) {
        val position = items.indexOf(item)
        if (position >= 0) {
            onUpdate(item)
            notifyItemChanged(position)
        }
    }

    private fun loadingView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(loadingResource(), parent, false)
    }

    // Open and abstract functions
    open fun onUpdate(item: T) {}

    open fun loadingResource(): Int {
        return R.layout.item_loading
    }

    abstract fun resource(viewType: Int): Int
    abstract fun createHolder(binding: B, viewType: Int): VH

    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_LOADING = 1
    }
}