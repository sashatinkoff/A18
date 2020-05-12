package com.isidroid.a18.ui

import android.content.Intent
import android.os.Bundle
import com.isidroid.a18.App
import com.isidroid.a18.GlideApp
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.PictureHandler
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val pictureHandler = PictureHandler(app = App.instance,
        debugCallback = {
            Timber.i("adsfsdfd $it")
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.isActionSend()) {
            Tasks.io(
                doWork = { pictureHandler.result(101, intent)?.list?.first() },
                onComplete = {
                    val info = it!!
                    GlideApp.with(imageview).load(info.localPath).into(imageview)
                },
                onError = { Timber.e("adsfsdfd ${it.message}") }
            )
        }
    }
}

fun Intent.isActionSend() = action == Intent.ACTION_SEND || action == Intent.ACTION_SEND_MULTIPLE