package com.isidroid.a18

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.databinding.ViewDataBinding
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.databinding.SampleItemBinding
import com.isidroid.utils.BindActivity
import com.isidroid.utils.adapters.CoreBindAdapter
import com.isidroid.utils.utils.YSpan
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.toHexString
import timber.log.Timber
import java.util.*


class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        textview.movementMethod = LinkMovementMethod.getInstance()
        textview.text = YSpan(this)
            .append("name")
            .append("==Sasha==")
            .onclick { Timber.i("clicked on Sasha/$it") }
            .append(" end")
            .build()

        recyclerview.adapter = adapter

//        name(name = "Red", bgColor = Color.parseColor("#FF0000"))
//        name(name = "Green", bgColor = Color.parseColor("#009688"))
//        name(name = "White", bgColor = Color.parseColor("#FFFFFF"))
//        name(name = "BLACK", bgColor = Color.parseColor("#000000"))
    }

    fun name(name: String, bgColor: Int) {
        val color = Color.rgb(
            255 - Color.red(bgColor),
            255 - Color.green(bgColor),
            255 - Color.blue(bgColor)
        )

        adapter.add(
            Item(
                bgColor = bgColor,
                color = if (isBrightColor(bgColor)) Color.BLACK else Color.WHITE,
                bgName = name,
                colorName = ""
            )
        )
    }

    fun isBrightColor(color: Int): Boolean {
        if (android.R.color.transparent == color) return true
        var rtnValue = false
        val rgb = intArrayOf(
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
        val brightness = Math.sqrt(
            rgb[0] * rgb[0] * .241 + (rgb[1]
                    * rgb[1] * .691) + rgb[2] * rgb[2] * .068
        ).toInt()
        // color is light
        if (brightness >= 200) {
            rtnValue = true
        }
        return rtnValue
    }


    private data class Item(
        val bgColor: Int,
        val color: Int,
        val bgName: String,
        val colorName: String
    )

    private class Adapter : CoreBindAdapter<Item>() {
        override fun resource(viewType: Int) = R.layout.sample_item
        override fun onBindHolder(binding: ViewDataBinding, position: Int) {
            Timber.i("onBindHolder $binding")
            (binding as? SampleItemBinding)?.apply {
                val item = items[position]

                textview.text = YSpan(root.context)
                    .append("background=${item.bgName} #{${item.bgColor.toHexString()}}")
                    .br()
                    .append("color=${item.colorName} #{${item.color.toHexString()}}")
                    .build()

                bgView.setBackgroundColor(item.bgColor)
                colorView.setTextColor(item.color)
                colorView.text = UUID.randomUUID().toString()
            }
        }
    }
}