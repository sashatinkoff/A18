package com.isidroid.a18

import android.os.Bundle
import com.isidroid.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    override fun onCreateViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edittext.setText("0.0")
        CostInputFilter(edittext)
                .withMaxFractionLength(2)
                .withTextSelected(true)
                .create()
    }
}