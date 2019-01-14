package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.media.ExifInterface
import android.os.Bundle
import android.os.Debug
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.viewmodel.TakePictureViewModel
import com.isidroid.utils.BindActivity
import com.isidroid.utils.utils.views.BottomsheetHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    private lateinit var takepictureViewModel: TakePictureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(CompositePermissionListener()).check()

        btnSubmit.setOnClickListener {
            //            takepictureViewModel.pickGallery(this)
//            memory()

            createBottomsheet(profilesSheet)
                    .withDim(null)
                    .create()
                    .expand()
        }
    }

    override fun onBackPressed() {
        if (bottomsheetHelper?.isExpanded() == true) bottomsheetHelper?.collapse()
        else super.onBackPressed()
    }

    private fun memory() {
        val nativeHeapSize = Debug.getNativeHeapSize() / (1024)
        val nativeHeapFreeSize = Debug.getNativeHeapFreeSize() / (1024)
        val usedMem = nativeHeapSize - nativeHeapFreeSize
        val usedMemInPercentage = usedMem * 100 / nativeHeapSize

        Timber.i("nativeHeapSize=$nativeHeapSize, nativeHeapFreeSize=$nativeHeapFreeSize, " +
                "usedMem=$usedMem, usedMemInPercentage=$usedMemInPercentage")
    }

    override fun onCreateViewModel() {
        takepictureViewModel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        takepictureViewModel.error.observe(this, Observer {
            Timber.e(it)
        })
        takepictureViewModel.imageInfo.observe(this, Observer {
            //            Glide.with(this).load(it.result?.localPath ?: "").into(imageview)

            memory()
            Timber.i("${it.result}")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        takepictureViewModel.onResult(requestCode, data)
    }
}