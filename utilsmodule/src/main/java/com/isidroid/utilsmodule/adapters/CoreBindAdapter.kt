package com.isidroid.utilsmodule.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.isidroid.utilsmodule.R

abstract class CoreBindAdapter<T, B : ViewDataBinding> : RecyclerView.Adapter<CoreHolder>() {
    private var loadMoreCallback: (() -> Unit)? = null
    protected var hasMore = false
    val items = mutableListOf<T>()

    fun onLoadMore(callback: (() -> Unit)) = apply { this.loadMoreCallback = callback }

    override fun getItemCount(): Int {
        var size = items.size
        if (hasMore) size++
        return size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size && hasMore && items.size > 0) VIEW_TYPE_LOADING
        else VIEW_TYPE_NORMAL
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> CoreLoadingHolder(loadingView(parent))
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding: B = DataBindingUtil.inflate(inflater, resource(viewType), parent, false)
                createHolder(binding, viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: CoreHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_NORMAL -> updateViewHolder(holder, position)
            VIEW_TYPE_LOADING -> loadMore()
        }
    }

    private fun updateViewHolder(holder: CoreHolder, position: Int) {
        try {
            (holder as? CoreBindHolder<T, B>)?.bind(items[position])
        } catch (e: Exception) {
        }
    }

    fun add(vararg items: T) = apply { this.items.addAll(items) }

    fun update(item: T) {
        val position = items.indexOf(item)
        if (position >= 0) {
            onUpdate(item)
            notifyItemChanged(position)
        }
    }

    fun update(items: List<T>, hasMore: Boolean) {
        this.hasMore = hasMore
        val size = this.items.size
        val inserted = items.size
        this.items.addAll(items)
        notifyItemRangeInserted(size, inserted)

        if (hasMore && this.items.size + 1 == itemCount) notifyItemChanged(itemCount - 1)
    }

    private fun loadingView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(loadingResource(), parent, false)
    }

    fun loadMore() {
        Handler().postDelayed({ loadMoreCallback?.invoke() }, 500)
    }

    // Open and abstract functions
    open fun onUpdate(item: T) {}

    open fun loadingResource(): Int {
        return R.layout.item_loading
    }

    abstract fun resource(viewType: Int): Int
    abstract fun createHolder(binding: B, viewType: Int): CoreHolder


    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_LOADING = 1
    }
}