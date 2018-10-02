package com.isidroid.a18.sample.adapters

import com.google.android.material.chip.Chip
import com.isidroid.a18.databinding.ItemSampleCustomBinding
import com.isidroid.utils.adapters.CoreBindHolder

class CustomHolder(b: ItemSampleCustomBinding) : CoreBindHolder<String, ItemSampleCustomBinding>(b) {
    private val icon = "https://pp.userapi.com/c305900/u1827083/a_0c8c6072.jpg?ava=1"

    override fun onBind(item: String) {
        binding.value = item
        binding.checkboxName = "Some checkbox"

        (0 until 10).forEach {
            val chip = Chip(binding.root.context).apply {
                text = "$it). $item"
            }

            binding.chips.addView(chip)
        }
    }

}