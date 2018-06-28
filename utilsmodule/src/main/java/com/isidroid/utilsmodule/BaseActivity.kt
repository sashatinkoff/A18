package com.isidroid.utilsmodule

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onPause() {
        super.onPause()
        ViewsUtils.hideSoftKeyboard(this)
    }
}