package com.isidroid.a18.ui

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.isidroid.a18.App
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.perms.askPermission
import com.isidroid.pics.PictureHandler
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val handler = PictureHandler(
        app = App.instance,
        forceRotate = true
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) { }
        btnStart.setOnClickListener { handler.pickGallery(caller = this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Tasks.io(
            doWork = {
                val results = handler.result(requestCode = requestCode, intent = data)
                val image = results?.list?.firstOrNull() ?: throw Exception("Empty results")
                BitmapFactory.decodeFile(image.localPath)
            },
            onComplete = {
                imageviewSimple.setImageBitmap(it)
            }
        )


        super.onActivityResult(requestCode, resultCode, data)
    }
}

