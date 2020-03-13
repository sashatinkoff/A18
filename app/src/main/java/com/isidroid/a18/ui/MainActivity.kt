package com.isidroid.a18.ui

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import com.isidroid.a18.utils.CameraxHelper
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.perms.askPermission
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.File


class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val cameraxHelper by lazy {
        CameraxHelper(
            caller = this,
            previewView = previewView,
            onPictureTaken = { file, uri ->
                Timber.i("Picture taken ${file.absolutePath}, uri=$uri")
            },
            onError = { Timber.e(it) },
            builderPreview = Preview.Builder().setTargetResolution(Size(200, 200)),
            builderImageCapture = ImageCapture.Builder().setTargetResolution(Size(200, 200)),
            filesDirectory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "camerax_sample"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE) {}

        btnCamera.setOnClickListener { cameraxHelper.takePicture() }
        btnSwitchLenses.setOnClickListener { cameraxHelper.changeCamera() }

        cameraxHelper.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        cameraxHelper.start()
    }
}

