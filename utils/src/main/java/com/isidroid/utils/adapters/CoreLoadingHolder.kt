package com.isidroid.utils.adapters

import android.view.View

open class CoreLoadingHolder(v: View) : CoreHolder(v) {
    open fun bind(position: Int) {}
}