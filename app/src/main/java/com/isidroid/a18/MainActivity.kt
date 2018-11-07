package com.isidroid.a18

import android.os.Bundle
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.utils.views.CostInputFilter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    override fun onCreateViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CostInputFilter(edittext)
    }
}