package com.isidroid.utilsmodule

import android.support.v7.app.AppCompatActivity
import com.isidroid.utilsmodule.views.Utils

abstract class BaseActivity : AppCompatActivity() {
    override fun onPause() {
        super.onPause()
        Utils.hideSoftKeyboard(this)
    }
}