package com.isidroid.utils.utils.views

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
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

inline fun AppCompatEditText.onAfterChanged(crossinline action: (Editable?) -> Unit) = addTextWatcherListener(after = action)
inline fun AppCompatEditText.onBeforeChange(crossinline action: (CharSequence?, Int, Int, Int) -> Unit) = addTextWatcherListener(before = action)
inline fun AppCompatEditText.onTextChanged(crossinline action: (CharSequence?, Int, Int, Int) -> Unit) = addTextWatcherListener(onChanged = action)
inline fun AppCompatEditText.addTextWatcherListener(
        crossinline after: (editable: Editable?) -> Unit = {},
        crossinline before: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
        crossinline onChanged: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> }
) {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = after(s)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = before(s, start, count, after)
        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = onChanged(s, start, count, after)
    }

    addTextChangedListener(listener)
}