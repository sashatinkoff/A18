package com.isidroid.a18.sample.adapters

import androidx.databinding.ViewDataBinding
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ItemSampleCarBinding
import com.isidroid.a18.databinding.ItemSampleCustomBinding
import com.isidroid.a18.databinding.ItemSamplePersonBinding
import com.isidroid.utils.adapters.CoreBindAdapter
import com.isidroid.utils.adapters.CoreHolder

const val TYPE_PERSON = 100
const val TYPE_CAR = 200
const val TYPE_CUSTOM = 300


class Adapter : CoreBindAdapter<String>() {
    override fun onUpdateHolder(binding: ViewDataBinding, position: Int) {
        val item = items[position]
        when (binding) {
            is ItemSamplePersonBinding -> binding.name = item
            is ItemSampleCarBinding -> binding.model = item
            is ItemSampleCustomBinding -> binding.value = item
        }
    }

    override fun onReset() {
//        add("@Sasha", "Vaz", "Toyota", "hyunday")
//        add("#BREAK IT")
//        add("@Dima", "Hyundai", "UAZ", "15")
        add("#BREAK IT222")

    }

//    override fun getItemViewType(position: Int): Int {
//        return when {
//            items[position].startsWith("@") -> TYPE_PERSON
//            items[position].startsWith("#") -> TYPE_CUSTOM
//            else -> TYPE_CAR
//        }
//    }


    override fun resource(viewType: Int): Int {
        return when (viewType) {
            TYPE_PERSON -> R.layout.item_sample_person
            TYPE_CAR -> R.layout.item_sample_car
            else -> R.layout.item_sample_custom
        }
    }

    override fun createHolder(binding: ViewDataBinding, viewType: Int): CoreHolder {
        return when (viewType) {
            TYPE_CUSTOM -> CustomHolder(binding as ItemSampleCustomBinding)
            else -> super.createHolder(binding, viewType)
        }
    }
}

