package com.isidroid.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

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
        crossinline after: (Editable?) -> Unit = {},
        crossinline before: (CharSequence?, Int, Int, Int) -> Unit = { _, _, _, _ -> },
        crossinline onChanged: (CharSequence?, Int, Int, Int) -> Unit = { _, _, _, _ -> }
) {
    val listener = object : TextWatcher {
        override fun afterTextChanged(e: Editable?) = after(e)
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = before(s, start, count, after)
        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = onChanged(s, start, count, after)
    }

    addTextChangedListener(listener)
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

fun Snackbar.setTextColor(color: Int): Snackbar {
    val contentLayout = (view as ViewGroup).getChildAt(0) as? SnackbarContentLayout
    contentLayout?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.setTextColor(color)
    return this
}