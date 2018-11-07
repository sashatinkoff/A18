package com.isidroid.a18

import android.os.Bundle
import android.os.Handler
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.isidroid.a18.databinding.ItemSampleBinding
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.adapters.CoreBindAdapter
import com.isidroid.utils.utils.views.CostInputFilter
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*


class MainActivity : BaseActivity() {
    override fun onCreateViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}