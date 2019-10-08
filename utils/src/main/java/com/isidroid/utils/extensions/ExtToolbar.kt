package com.isidroid.utils.extensions

import android.widget.TextView
import androidx.appcompat.widget.Toolbar

fun Toolbar.subtitle(): TextView? {
    val textviews = mutableListOf<TextView>()
    (0..childCount).forEach { pos ->
        (getChildAt(pos) as? TextView)
            ?.let { if (it.contentDescription == null) textviews.add(it) }
    }


    return textviews.lastOrNull()
}

fun Toolbar.subtitleDropdown(onClick: ((TextView) -> Unit)? = null): TextView? {
    return subtitle()?.let { view ->
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.isidroid.utils.R.drawable.ic_arrow_drop_down, 0)
        view.setOnClickListener { onClick?.invoke(it as TextView) }
        view
    }
}