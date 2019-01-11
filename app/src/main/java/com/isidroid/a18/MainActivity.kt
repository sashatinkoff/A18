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
            takepictureViewModel.pickGallery(this)
        }
    }

    override fun onCreateViewModel() {
        takepictureViewModel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        takepictureViewModel.error.observe(this, Observer {
            Timber.e(it)
        })
        takepictureViewModel.imageInfo.observe(this, Observer {
            imageview.setImageBitmap(it.result?.bitmap)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        takepictureViewModel.onResult(requestCode, data)
    }


}