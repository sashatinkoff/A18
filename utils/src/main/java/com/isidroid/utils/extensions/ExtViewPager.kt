package com.isidroid.utils.extensions

import androidx.viewpager.widget.ViewPager

inline fun ViewPager.onSelected(crossinline action: (position: Int) -> Unit) = addListener(onSelected = action)
inline fun ViewPager.addListener(crossinline onSelected: (position: Int) -> Unit = {}): ViewPager {

    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) = onSelected(position)
    }

    addOnPageChangeListener(listener)
    return this
}
