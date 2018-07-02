package com.isidroid.utilsmodule

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

object ViewsUtils {
    fun hideSoftKeyboard(activity: AppCompatActivity?) {
        if (activity == null || activity.isFinishing) return
        val view = activity.findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}