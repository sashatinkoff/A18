package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.extensions.put
import com.isidroid.a18.extensions.showFragmentDialog
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        input.setText("eylUOJgs9Ig")

        buttonYoutube.setOnClickListener {
            showDialog(true)
        }

        buttonWebview.setOnClickListener {
            showDialog(false)
        }
    }

    private fun showDialog(isYoutube: Boolean) {
        val id = input.text.toString()
        showFragmentDialog(
            tag = "youtube",
            fragment = VideoDialog().put("id", id).put("isYoutube", isYoutube)
        )
    }
}