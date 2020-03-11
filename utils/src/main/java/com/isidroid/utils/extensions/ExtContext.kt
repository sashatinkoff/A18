package com.isidroid.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.AttrRes
import kotlin.math.roundToInt

fun Context.resourceFromAttr(@AttrRes attr: Int) = if (attr == 0) 0 else with(TypedValue()) {
    theme.resolveAttribute(attr, this, true)
    resourceId
}

fun Context.colorFromAttr(@AttrRes attr: Int) = if (attr == 0) 0 else with(TypedValue()) {
    theme.resolveAttribute(attr, this, true)
    data
}

fun Context.screenWidthPx() = Resources.getSystem().displayMetrics.widthPixels
fun Context.screenHeightPx() = Resources.getSystem().displayMetrics.heightPixels

fun Context.dpToPx(dp: Int) =
    with(resources.displayMetrics) { (dp * (xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt() }

fun Context.pxToDp(px: Int) = with(resources.displayMetrics) {
    (px / (xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}
