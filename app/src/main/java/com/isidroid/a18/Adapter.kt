package com.isidroid.a18

import androidx.databinding.ViewDataBinding
import com.isidroid.a18.databinding.SampleItemBinding
import com.isidroid.utils.adapters.CoreBindAdapter
import timber.log.Timber


class Adapter : CoreBindAdapter<String>() {
    init {
//        (0..25).forEach { items.add("Item $it") }
    }

    override val hasEmpty = true
    override val emptyResource = R.layout.sample_item_empty

    override fun resource(viewType: Int) = R.layout.sample_item
    override fun onBindHolder(binding: ViewDataBinding, position: Int) {
//        (binding as? SampleItemBinding)?.apply { items[position] }
    }

    override fun getItemCount(): Int {
        Timber.i("workflow itemcount items=${items.size}, hasmore=$hasMore, isInserted=$isInserted")
        return super.getItemCount()
    }
}