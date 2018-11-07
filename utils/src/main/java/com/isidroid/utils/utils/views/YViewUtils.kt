package com.isidroid.utils.utils.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

object YViewUtils {
    fun hideSoftKeyboard(activity: AppCompatActivity?) {
        if (activity == null || activity.isFinishing) return
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun height(view: View): Int {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        return params.topMargin + params.bottomMargin + view.paddingTop + view.paddingBottom + view.height
    }

    fun findViewsByTag(root: View, tag: String?, callback: ((view: View) -> Unit)? = null): ArrayList<View> {
        val views = ArrayList<View>()
        if (root !is ViewGroup) return views

        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup)
                views.addAll(findViewsByTag(child, tag, callback))

            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                callback?.invoke(child)
                views.add(child)
            }
        }

        return views
    }


}

