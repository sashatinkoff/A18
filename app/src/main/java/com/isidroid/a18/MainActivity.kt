package com.isidroid.a18

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.logger.Diagnostics
import com.isidroid.logger.FileLogger
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.sample_main.*
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.util.*


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CompositePermissionListener()).check()

        btnSave.setOnClickListener { Timber.i("save") }
        btnPdf.setOnClickListener { Timber.i("pdf") }
        btnCamera.setOnClickListener { Timber.i("camera") }

        btnOpen.setOnClickListener {
        }
    }

}