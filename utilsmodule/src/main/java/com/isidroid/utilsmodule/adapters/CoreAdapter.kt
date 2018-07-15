package com.isidroid.utilsmodule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView

abstract class CoreAdapter<T, VH : CoreHolder<T>> : RecyclerView.Adapter<VH>() {
    var items: MutableList<T> = ArrayList()

    override fun onBindViewHolder(holder: VH, position: Int) {
        try {
            holder.update(items[position])
        } catch (e: Exception) {
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    protected fun getView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(resourceId(viewType), parent, false)
    }

    fun update(data: List<T>, callback: ((items: MutableList<T>) -> Unit)? = null) {
        items.clear()
        items.addAll(data)
        callback?.let { it(items) }

        notifyDataSetChanged()
    }

    fun update(item: T, callback: ((items: MutableList<T>) -> Unit)? = null) {
        items.add(item)
        callback?.let { it(items) }

        notifyDataSetChanged()
    }

    protected abstract fun resourceId(viewType: Int): Int
}
