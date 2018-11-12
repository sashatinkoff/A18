package com.isidroid.a18.backdrop

interface BackdropListener {
    fun onCollapse()
    fun onExpand()
    fun onExpandStarted()
    fun onCollapseStarted()
    fun onExpandDone()
    fun onCollapseDone()
    fun onDestroy()
    fun onDestroyStarted()
}

inline fun Backdrop.onCollapse(crossinline action: () -> Unit) = addListener(collapse = action)
inline fun Backdrop.onExpand(crossinline action: () -> Unit) = addListener(expand = action)
inline fun Backdrop.onExpandStarted(crossinline action: () -> Unit) = addListener(onExpanding = action)
inline fun Backdrop.onCollapseStarted(crossinline action: () -> Unit) = addListener(onCollapsing = action)
inline fun Backdrop.onExpandDone(crossinline action: () -> Unit) = addListener(onExpanded = action)
inline fun Backdrop.onCollapseDone(crossinline action: () -> Unit) = addListener(onCollapsed = action)
inline fun Backdrop.onDestroy(crossinline action: () -> Unit) = addListener(onDestroy = action)
inline fun Backdrop.onDestroyStarted(crossinline action: () -> Unit) = addListener(onDestroyStarted = action)

inline fun Backdrop.addListener(
        crossinline collapse: () -> Unit = {},
        crossinline expand: () -> Unit = {},
        crossinline onExpanding: () -> Unit = {},
        crossinline onCollapsing: () -> Unit = {},
        crossinline onExpanded: () -> Unit = {},
        crossinline onCollapsed: () -> Unit = {},
        crossinline onDestroy: () -> Unit = {},
        crossinline onDestroyStarted: () -> Unit = {}
): Backdrop {

    val listener = object : BackdropListener {
        override fun onCollapse() = collapse()
        override fun onExpand() = expand()
        override fun onExpandStarted() = onExpanding()
        override fun onCollapseStarted() = onCollapsing()
        override fun onExpandDone() = onExpanded()
        override fun onCollapseDone() = onCollapsed()
        override fun onDestroy() = onDestroy()
        override fun onDestroyStarted() = onDestroyStarted()
    }

    addListener(listener)
    return this
}