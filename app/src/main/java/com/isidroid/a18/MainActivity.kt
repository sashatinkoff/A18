package com.isidroid.a18

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Debug
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.viewmodel.TakePictureViewModel
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.File
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    private lateinit var takepictureViewModel: TakePictureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(CompositePermissionListener()).check()

        bitmap()
        btnSubmit.setOnClickListener { bitmap() }
    }

    private fun bitmap() {
        val width = 22000
        val height = 22000
        val bitmap = createBitmap(width, height)
        Timber.i("size=[${bitmap?.width}, ${bitmap?.height}]")
    }

    private fun createBitmap(width: Int, height: Int): Bitmap? {
        var success: Boolean
        var sampleSize = 0
        var result: Bitmap? = null

        System.gc()

        do {
            try {
                sampleSize++
                val bWidth = width / sampleSize
                val bHeight = height / sampleSize

                result = Bitmap.createBitmap(bWidth, bHeight, Bitmap.Config.ARGB_8888)
                success = true
            } catch (e: java.lang.OutOfMemoryError) {
                success = false
            }
        } while (!success)

        return result
    }

    private fun start() {
        AlertDialog.Builder(this)
                .setItems(arrayOf("Pick image", "Pick pdf")) { _, pos ->
                    when (pos) {
                        0 -> takepictureViewModel.pickGallery(this)
                        1 -> takepictureViewModel.pick(this, "application/pdf")
                    }
                }
                .show()
    }

    override fun onCreateViewModel() {
        takepictureViewModel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        takepictureViewModel.error.observe(this, Observer {
            Timber.tag("pickresults").e(it)
        })
        takepictureViewModel.imageInfo.observe(this, Observer {
            //            Glide.with(this).load(it.result?.localPath ?: "").into(imageview)
            val file = File(it.result?.localPath)
            Timber.tag("pickresults").i("exists=${file.exists()}, path=${file.absolutePath}")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        takepictureViewModel.onResult(requestCode, data)
    }
}