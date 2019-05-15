package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.viewmodel.TakePictureViewModel
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.sample_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    private val viewmodel: TakePictureViewModel by lazy {
        ViewModelProviders.of(this).get(TakePictureViewModel::class.java).create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener()).check()

        btnSave.setOnClickListener { Timber.i("save") }
        btnPdf.setOnClickListener { Timber.i("pdf") }
        btnCamera.setOnClickListener {
            Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : CompositePermissionListener(){
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        viewmodel.pickGallery(this@MainActivity, false)
                    }
                }).check()
        }

        btnOpen.setOnClickListener {
        }
    }

    override fun onCreateViewModel() {
        super.onCreateViewModel()
        viewmodel.error.observe(this, Observer { Timber.tag("asdasdasdasd").e(it) })
        viewmodel.imageInfo.observe(this, Observer { Timber.i("asdasdasdasd ${it.result?.firstOrNull()}") })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewmodel.onResult(resultCode, data)
    }
}