package com.isidroid.a18

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.ViewModelProviders
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.viewmodel.TakePictureViewModel
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import android.R.attr.data
import android.content.ClipData


class MainActivity : BindActivity<ActivityMainBinding>() {
    private lateinit var viewmodel: TakePictureViewModel

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

        btnSave.setOnClickListener { viewmodel.pickGallery(this, true) }
    }

    override fun onCreateViewModel() {
        viewmodel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imagesPath = data?.clipData
        Timber.i("onActivityResult $data, path=$imagesPath")


        viewmodel.onResult(requestCode, data)
    }
}