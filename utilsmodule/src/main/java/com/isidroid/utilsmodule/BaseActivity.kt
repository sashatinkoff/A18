package com.isidroid.utilsmodule

import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onPause() {
        super.onPause()
        ViewsUtils.hideSoftKeyboard(this)
    }
}