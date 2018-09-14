package com.isidroid.a18.backdrop

import android.animation.Animator

interface BackdropListener {
    fun onCollapse()
    fun onExpand()
    fun onExpandStarted()
    fun onCollapseStarted()
    fun onExpandDone()
    fun onCollapseDone()
    fun onDestroy()
}

open class BackdropAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) {}
    override fun onAnimationEnd(p0: Animator?) {}
    override fun onAnimationCancel(p0: Animator?) {}
    override fun onAnimationStart(p0: Animator?) {}
}


inline fun Backdrop2.onCollapse(crossinline action: () -> Unit) = addListener(collapse = action)
inline fun Backdrop2.onExpand(crossinline action: () -> Unit) = addListener(expand = action)
inline fun Backdrop2.onExpandStarted(crossinline action: () -> Unit) = addListener(onExpanding = action)
inline fun Backdrop2.onCollapseStarted(crossinline action: () -> Unit) = addListener(onCollapsing = action)
inline fun Backdrop2.onExpandDone(crossinline action: () -> Unit) = addListener(onExpanded = action)
inline fun Backdrop2.onCollapseDone(crossinline action: () -> Unit) = addListener(onCollapsed = action)
inline fun Backdrop2.onDestroy(crossinline action: () -> Unit) = addListener(onDestroy = action)

inline fun Backdrop2.addListener(
        crossinline collapse: () -> Unit = {},
        crossinline expand: () -> Unit = {},
        crossinline onExpanding: () -> Unit = {},
        crossinline onCollapsing: () -> Unit = {},
        crossinline onExpanded: () -> Unit = {},
        crossinline onCollapsed: () -> Unit = {},
        crossinline onDestroy: () -> Unit = {}
): Backdrop2 {

    val listener = object : BackdropListener {
        override fun onCollapse() = collapse()
        override fun onExpand() = expand()
        override fun onExpandStarted() = onExpanding()
        override fun onCollapseStarted() = onCollapsing()
        override fun onExpandDone() = onExpanded()
        override fun onCollapseDone() = onCollapsed()
        override fun onDestroy() = onDestroy()
    }

    addListener(listener)
    return this
}