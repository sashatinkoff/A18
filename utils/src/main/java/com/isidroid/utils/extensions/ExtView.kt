package com.isidroid.utils.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager

fun View.enable(enabled: Boolean, alpha: Float = .6f) = apply {
    this.alpha = if (enabled) 1f else alpha
    isEnabled = enabled
}

fun View.visible(isVisible: Boolean, isInvisible: Boolean = true) {
    visibility = when {
        isVisible -> View.VISIBLE
        isInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.height(onlyHeight: Boolean): Int {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    val offsets = params.topMargin + params.bottomMargin + paddingTop + paddingBottom
    return height + if (onlyHeight) 0 else offsets
}

fun View.findViewsByTag(tag: String?, callback: ((view: View) -> Unit)? = null): ArrayList<View> {
    val views = ArrayList<View>()
    if (this !is ViewGroup) return views

    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup)
            views.addAll(child.findViewsByTag(tag, callback))

        val tagObj = child.tag
        if (tagObj != null && tagObj == tag) {
            callback?.invoke(child)
            views.add(child)
        }
    }

    return views
}

fun View.childrenAll(): ArrayList<View> {
    val views = ArrayList<View>()
    if (this !is ViewGroup) return views
    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) views.addAll(child.childrenAll())
    }

    return views
}


fun View.applyOnChildren(tag: String?, callback: ((view: View) -> Unit)) {
    if (this !is ViewGroup) return
    tag ?: return

    val childCount = childCount
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child.tag == tag) callback.invoke(child)
        child.applyOnChildren(tag, callback)
    }
}

fun View.expand(duration: Long, targetHeight: Int) = apply {
    visibility = View.VISIBLE
    val valueAnimator = ValueAnimator.ofInt(height, targetHeight)
    valueAnimator.addUpdateListener { animation ->
        layoutParams.height = animation.animatedValue as Int
        requestLayout()
    }
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = duration
    valueAnimator.start()
}

fun View.collapse(duration: Long) = apply {
    val prevHeight = height
    val valueAnimator = ValueAnimator.ofInt(prevHeight, 0)
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.addUpdateListener { animation ->
        layoutParams.height = animation.animatedValue as Int
        requestLayout()
    }
    valueAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationEnd(animation: Animator?) {
            visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}

    })
    valueAnimator.interpolator = DecelerateInterpolator()
    valueAnimator.duration = duration
    valueAnimator.start()
}