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
    private var hasMore = false
    protected open val loadingResource: Int = R.layout.item_loading
    protected open val hasInitialLoading = false

    val items = mutableListOf<T>()

    fun onLoadMore(callback: (() -> Unit)) = apply { this.loadMoreCallback = callback }
    fun add(vararg items: T) = apply { this.items.addAll(items) }

    fun create() = apply {
        hasMore = hasInitialLoading
        onCreate()
    }

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> createLoadingHolder(loadingView(parent))
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
            VIEW_TYPE_LOADING -> updateLoadingViewHolder(holder as CoreLoadingHolder, position)
        }
    }

    private fun updateLoadingViewHolder(holder: CoreLoadingHolder, position: Int) {
        holder.bind(position)
        loadMore()
    }

    private fun updateViewHolder(holder: CoreHolder, position: Int) {
        try {
            (holder as? CoreBindHolder<T, B>)?.bind(items[position])
        } catch (e: Exception) {
        }
    }

    fun update(item: T) {
        findPosition(item) {
            onUpdate(item)
            notifyItemChanged(it)
        }
    }

    fun remove(item: T) {
        findPosition(item) {
            items.remove(item)
            onRemove(item)
            notifyItemRemoved(it)
        }
    }

    private fun findPosition(item: T, callback: (pos: Int) -> Unit) {
        val position = items.indexOf(item)
        if (position >= 0) callback.invoke(position)
    }

    fun insert(items: List<T>, hasMore: Boolean) {
        this.hasMore = hasMore
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun loadingView(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(loadingResource, parent, false)
    }

    fun loadMore() {
        synchronized(this) {
            Handler().postDelayed({ loadMoreCallback?.invoke() }, 500)
        }
    }

    fun reset() {
        hasMore = hasInitialLoading
        items.clear()

        onReset()
        notifyDataSetChanged()

        loadMore()
    }

    // Open and abstract functions
    open fun createLoadingHolder(view: View): CoreLoadingHolder = CoreLoadingHolder(view)

    open fun onCreate() {}
    open fun onReset() {}

    open fun onUpdate(item: T) {}
    open fun onRemove(item: T) {}


    abstract fun resource(viewType: Int): Int
    abstract fun createHolder(binding: B, viewType: Int): CoreHolder

    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_LOADING = 1
    }
}