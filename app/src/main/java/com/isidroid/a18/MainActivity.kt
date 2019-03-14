package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.viewmodel.TakePictureViewModel
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sample_main.*
import timber.log.Timber
import java.io.File


class MainActivity : BindActivity<ActivityMainBinding>() {
    private lateinit var viewmodel: TakePictureViewModel

    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action == Intent.ACTION_SEND || intent?.action == Intent.ACTION_SEND_MULTIPLE) handleSendData(intent)
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CompositePermissionListener()).check()


        val adapter = Adapter()
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter


        btnOpen.setOnClickListener {
            createBottomsheet(bottomSheet, coordinator) { it?.alpha(.5f) }.expand()
        }

        btnSave.setOnClickListener { viewmodel.pickGallery(this, true) }
        btnPdf.setOnClickListener { viewmodel.pick(this, "application/pdf") }
        btnCamera.setOnClickListener {
            viewmodel.takePicture(this)
        }

//        btnSave.setOnClickListener {
//            AlertDialog.Builder(this)
//                .setTitle("Greetings to you")
//                .setMessage("Hello, this is just a message with several lines\nWith the best wishes, just me")
//                .setPositiveButton("Positive", null)
//                .setNegativeButton("Negative", null)
//                .setNeutralButton("Neutral", null)
//                .show()
//        }
//
//        btnCamera.setOnClickListener {
//            val items = arrayListOf("First", "Second", "Last").toTypedArray()
//            AlertDialog.Builder(this)
//                .setTitle("Greetings to you")
//                .setMultiChoiceItems(items, null, null)
//                .setPositiveButton("Positive", null)
//                .setNegativeButton("Negative", null)
//                .setNeutralButton("Neutral", null)
//                .show()
//        }


        // show simple dialog with text
//        AlertDialog.Builder(this)
//            .setTitle("Greetings to you")
//            .setMessage("Hello, this is just a message with several lines\nWith the best wishes, just me")
//            .setPositiveButton("Positive", null)
//            .setNegativeButton("Negative", null)
//            .setNeutralButton("Neutral", null)
//            .show()
//
//        // show dialog with multiple checkboxes
//        val items = arrayListOf("First", "Second", "Last").toTypedArray()
//        AlertDialog.Builder(this)
//            .setTitle("Greetings to you")
//            .setMultiChoiceItems(items, null, null)
//            .setPositiveButton("Positive", null)
//            .setNegativeButton("Negative", null)
//            .setNeutralButton("Neutral", null)
//            .show()
    }

    override fun onCreateViewModel() {
        viewmodel = ViewModelProviders.of(this).get(TakePictureViewModel::class.java)
        viewmodel.error.observe(this, Observer {
            Timber.e("IMAGE INFO OBSERVED ERROR ${it.message}")
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
        viewmodel.imageInfo.observe(this, Observer { info ->
            info.result?.forEach {
                val file = File(it.localPath)
                Timber.tag("imageinforesult").i("${file.absolutePath}/${file.exists()}")
                Glide.with(imageview).load(it.localPath).into(imageview)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewmodel.onResult(requestCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == Intent.ACTION_SEND || intent?.action == Intent.ACTION_SEND_MULTIPLE) handleSendData(intent)
    }

    private fun handleSendData(intent: Intent) {
        viewmodel.onResult(PictureConfig.get().codePick, intent)
    }
}