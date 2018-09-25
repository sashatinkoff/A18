package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.SamplePageBinding
import com.isidroid.utils.BaseActivity


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, SamplePage(), "sdfsdf")
            commitAllowingStateLoss()
        }
    }
}