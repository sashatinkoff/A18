package com.isidroid.a18

import android.view.View
import androidx.databinding.ViewDataBinding
import com.isidroid.a18.databinding.SampleItemBinding
import com.isidroid.a18.sticky.IStickyHeaderAdapter
import com.isidroid.utils.adapters.CoreBindAdapter
import java.util.*


private const val HEADER1 = 100
private const val HEADER2 = 101

class Adapter : CoreBindAdapter<String>() {
    init {
        (0..25).forEach { items.add("Item $it") }
    }

    override fun resource(viewType: Int) = when (viewType) {
        HEADER1 -> R.layout.sample_header1
        HEADER2 -> R.layout.sample_header2
        else -> R.layout.sample_item
    }

    override fun onBindHolder(binding: ViewDataBinding, position: Int) {
        (binding as? SampleItemBinding)?.name = items[position]
    }
}