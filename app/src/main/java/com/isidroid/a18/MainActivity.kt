package com.isidroid.a18

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.isidroid.a18.sample.adapters.Adapter
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.adapters.CoreBindAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        container.layoutManager = LinearLayoutManager(this)
        container.adapter = Adapter()

        materialButton.setOnClickListener {
            (container.adapter as CoreBindAdapter<*>).reset()
        }
    }
}

