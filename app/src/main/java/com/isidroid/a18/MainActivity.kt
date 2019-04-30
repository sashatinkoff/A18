package com.isidroid.a18

import android.Manifest
import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.sample_main.*
import timber.log.Timber


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