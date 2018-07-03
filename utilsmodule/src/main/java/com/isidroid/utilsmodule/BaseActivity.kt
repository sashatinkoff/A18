package com.isidroid.utilsmodule

import androidx.appcompat.app.AppCompatActivity
import com.isidroid.utilsmodule.utils.YViewUtils

abstract class BaseActivity : AppCompatActivity() {
    override fun onPause() {
        super.onPause()
        YViewUtils.hideSoftKeyboard(this)
    }
}