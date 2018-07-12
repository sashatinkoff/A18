package com.isidroid.utilsmodule.adapters

import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

abstract class CoreHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var activity: AppCompatActivity = itemView.context as AppCompatActivity

    fun update(item: T) {
        onUpdate(item)
    }

    protected abstract fun onUpdate(item: T)
}
