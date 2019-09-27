package com.isidroid.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity


object YViewUtils {
    fun hideSoftKeyboard(activity: AppCompatActivity?) {
        if (activity == null || activity.isFinishing) return
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun height(view: View, onlyHeight: Boolean): Int {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        val offsets = params.topMargin + params.bottomMargin + view.paddingTop + view.paddingBottom
        return view.height + if (onlyHeight) 0 else offsets
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

    fun findChildren(root: View?): ArrayList<View> {
        val views = ArrayList<View>()
        if (root !is ViewGroup) return views
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) views.addAll(findChildren(child))
        }

        return views
    }


    fun executeOnViews(root: View, tag: String?, callback: ((view: View) -> Unit)) {
        if (root !is ViewGroup) return
        tag ?: return

        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child.tag == tag) callback.invoke(child)
            executeOnViews(child, tag, callback)
        }
    }

}
