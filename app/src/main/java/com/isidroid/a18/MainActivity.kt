package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isidroid.utilsmodule.Diagnostics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Diagnostics.i(this, "scree=" + App.component.screenUtils.width)
    }
}
