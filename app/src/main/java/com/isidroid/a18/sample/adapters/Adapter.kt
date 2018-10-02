package com.isidroid.a18.sample.adapters

import androidx.databinding.ViewDataBinding
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ItemSampleCarBinding
import com.isidroid.a18.databinding.ItemSamplePersonBinding
import com.isidroid.utils.adapters.CoreBindAdapter
import com.isidroid.utils.adapters.CoreHolder

const val TYPE_PERSON = 100
const val TYPE_CAR = 200


class Adapter : CoreBindAdapter<String>() {
    override fun onReset() {
        add("@Sasha", "Vaz", "Toyota", "hyunday")
        add("@Dima", "Hyundai", "UAZ", "15")
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].startsWith("@")) TYPE_PERSON
        else TYPE_CAR
    }

    override fun onUpdateHolder(binding: ViewDataBinding, item: String) {
        if (binding is ItemSamplePersonBinding) binding.name = item
        else if (binding is ItemSampleCarBinding) binding.model = item
    }

    override fun resource(viewType: Int): Int {
        return if (viewType == TYPE_PERSON) R.layout.item_sample_person
        else R.layout.item_sample_car
    }

    override fun createHolder(binding: ViewDataBinding, viewType: Int): CoreHolder {
        return if (viewType == TYPE_PERSON) Holder<String, ItemSamplePersonBinding>(binding as ItemSamplePersonBinding)
        else Holder<String, ItemSampleCarBinding>(binding as ItemSampleCarBinding)
    }
}

