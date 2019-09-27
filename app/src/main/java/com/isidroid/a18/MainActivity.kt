package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.sample.rest.ApiTest
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.sample_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logging()

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener())
            .check()
        pictures()

        btnSave.apply {
            text = "send"
            setOnClickListener {
                val url = "https://foodocity.page.link/V4SP"
                startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
            }
        }

    }

    private fun pictures() {
        btnSave.apply {
            text = "Camera"
            setOnClickListener { viewmodel.camera(this@MainActivity) }
        }

        btnPdf.apply {
            text = "Gallery"
            setOnClickListener { viewmodel.gallery(this@MainActivity) }
        }
    }

    private fun logging() {
        btnSave.apply {
            text = "Start logging"
            setOnClickListener { viewmodel.startLogging() }
        }

        btnPdf.apply {
            text = "Stop logging"
            setOnClickListener { viewmodel.stopLogging() }
        }
    }


    override fun onCreateViewModel() {
        viewmodel.intent.observe(this, Observer { startActivity(it) })
        viewmodel.pictureResults.observe(this, Observer { results ->
            results.list.firstOrNull()?.apply {
                //                imageview.setImageBitmap(BitmapFactory.decodeFile(localPath))

                Glide.with(imageview).load(localPath).into(imageview)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewmodel.onResult(requestCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}