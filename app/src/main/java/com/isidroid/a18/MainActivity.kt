package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.logger.Diagnostics
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.viewmodel.TakePictureViewModel
import com.isidroid.utils.BindActivity
import com.isidroid.utils.subscribeIoMain
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sample_main.*
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener()).check()


        btnOpen.setOnClickListener {
            Timber.i("click on button")
            createBottomsheet(bottomSheet, coordinator) { it?.alpha(.5f) }.expand()
        }

        btnSave.setOnClickListener { }
        btnPdf.setOnClickListener { }
    }

    override fun onCreateViewModel() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
}