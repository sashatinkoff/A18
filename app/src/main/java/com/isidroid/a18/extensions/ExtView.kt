package com.isidroid.a18.extensions

import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

fun View.applyOnChildren(tag: String? = null, callback: ((view: View) -> Unit)? = null) {
    if (this !is ViewGroup) return
    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (tag != null && child.tag == tag) callback?.invoke(child)
        child.applyOnChildren(tag, callback)
    }
}

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun View.enable(enabled: Boolean, alpha: Float = .6f) = apply {
    this.alpha = if (enabled) 1f else alpha
    isEnabled = enabled
}

fun View.visible(isVisible: Boolean, isHidden: Boolean = false) {
    visibility = when {
        isVisible -> View.VISIBLE
        isHidden -> View.INVISIBLE
        else -> View.GONE
    }
}