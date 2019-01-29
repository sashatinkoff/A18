package com.isidroid.a18

import android.Manifest
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
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.isidroid.pics.PictureConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import java.io.File


class MainActivity : BindActivity<ActivityMainBinding>() {
    private lateinit var viewmodel: TakePictureViewModel

    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (intent?.action == Intent.ACTION_SEND || intent?.action == Intent.ACTION_SEND_MULTIPLE) handleSendData(intent)
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener()).check()

        btnOpen.setOnClickListener {
            createBottomsheet(bottomSheet, coordinator) { it?.alpha(.5f) }.expand()
        }

        btnSave.setOnClickListener { viewmodel.pickGallery(this, false) }
        btnPdf.setOnClickListener { viewmodel.pick(this, "application/pdf") }
        btnCamera.setOnClickListener { viewmodel.takePicture(this) }
    }

    override fun onCreateViewModel() {
        viewmodel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        viewmodel.error.observe(this, Observer {
            Timber.e("IMAGE INFO OBSERVED ERROR ${it.message}")
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
        viewmodel.imageInfo.observe(this, Observer { info ->
            info.result?.forEach {
                val file = File(it.localPath)
                Timber.i("${file.absolutePath}/${file.exists()}")

                Glide.with(imageview).load(it.localPath).into(imageview)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewmodel.onResult(requestCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEND || intent?.action == Intent.ACTION_SEND_MULTIPLE) handleSendData(intent)
    }

    private fun handleSendData(intent: Intent) {
        viewmodel.onResult(PictureConfig.get().codePick, intent)
    }
}