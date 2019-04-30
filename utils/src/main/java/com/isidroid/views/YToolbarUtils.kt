package com.isidroid.views

import android.widget.TextView
import androidx.appcompat.widget.Toolbar

object YToolbarUtils {
    fun subtitle(toolbar: Toolbar): TextView? {
        val textviews = mutableListOf<TextView>()
        (0..toolbar.childCount).forEach { pos -> (toolbar.getChildAt(pos) as? TextView)?.let { textviews.add(it) } }
        return if (textviews.size == 2) textviews.last() else null
    }

    fun subtitleDropdown(toolbar: Toolbar, onClick: ((TextView) -> Unit)? = null): TextView? {
        return subtitle(toolbar)?.let { view ->
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, com.isidroid.utils.R.drawable.ic_arrow_drop_down, 0)
            view.setOnClickListener { onClick?.invoke(it as TextView) }
            view
        }
    }
}