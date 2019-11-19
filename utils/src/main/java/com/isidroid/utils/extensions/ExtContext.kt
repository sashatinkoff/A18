package com.isidroid.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
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

fun Context?.isConnected(wifi: Boolean = true, cellular: Boolean = true): Boolean {
    if (this == null) return false

    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && wifi -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && cellular -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}
