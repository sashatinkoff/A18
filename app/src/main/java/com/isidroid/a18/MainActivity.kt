package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.TakePictureViewModel
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.subscribeIoMain
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.CompositePermissionListener
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {
    lateinit var viewmodel: TakePictureViewModel
    override fun onCreateViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        PictureConfig.get().withContext(this).withPackage(BuildConfig.APPLICATION_ID)
        viewmodel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        viewmodel.imageInfo.observe(this, Observer {
            val result = it.result
            val intent = Intent().putExtra("s", result)
            val result2 = intent.getSerializableExtra("s")
            Timber.i("complete\n$result\n\n$result2")
        })


        materialButton.setOnClickListener {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : CompositePermissionListener() {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            viewmodel.pickGallery(this@MainActivity)
//                            viewmodel.takePicture(this@MainActivity)
                        }
                    })
                    .check()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (PictureConfig.get().isSuccess(requestCode, resultCode))
            viewmodel.onResult(requestCode, data)
    }
}