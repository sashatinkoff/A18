package com.isidroid.utils.utils.views

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.isidroid.utils.R

object YToolbarUtils {
    fun subtitle(toolbar: Toolbar): TextView? {
        var subtitleView: TextView? = null
        toolbar.post {
            (0..toolbar.childCount).reversed().forEach lit@{ pos ->
                val view = toolbar.getChildAt(pos)

                if (view is TextView) {
                    subtitleView = view
                    return@lit
                }
            }
        }
        return subtitleView
    }

    fun subtitleDropdown(toolbar: Toolbar, onClick: ((View) -> Unit)? = null) {
        subtitle(toolbar)?.let { view ->
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0)
            view.setOnClickListener { onClick?.invoke(it) }
        }
    }
}