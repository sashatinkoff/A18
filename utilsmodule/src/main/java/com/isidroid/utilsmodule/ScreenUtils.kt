package com.isidroid.utilsmodule

import android.app.Application
import android.content.res.Resources
import android.util.DisplayMetrics
import javax.inject.Inject

class ScreenUtils(private val application: Application) {
    val width: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    val height: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    fun dpToPx(dp: Int): Int {
        val displayMetrics = application.resources.displayMetrics
        val px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        return px
    }

    fun pxToDp(px: Int): Int {
        val displayMetrics = application.resources.displayMetrics
        val dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        return dp
    }

}