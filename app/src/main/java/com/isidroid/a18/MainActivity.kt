package com.isidroid.a18

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.utils.views.YToolbarUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity() {
    override fun onCreateViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.subtitle = "stop"
        YToolbarUtils.subtitleDropdown(toolbar) { view ->
            Toast.makeText(this, "OnClick", Toast.LENGTH_LONG).show()
            view.text = UUID.randomUUID().toString()
        }
    }
}