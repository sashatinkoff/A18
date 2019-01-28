package com.isidroid.a18

import android.graphics.Color
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        btnOpen.setOnClickListener {
            createBottomsheet(bottomSheet, coordinator)
            { dim ->
                dim
                        ?.alpha(.4f)
                        ?.interpolator(DecelerateInterpolator())
                        ?.color(Color.RED)
            }
                    .expand()
        }
    }
}