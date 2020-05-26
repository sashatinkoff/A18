package com.isidroid.a18.ui

import android.content.Intent
import android.os.Bundle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.isidroid.a18.App
import com.isidroid.a18.GlideApp
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.pics.PictureHandler
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.lang.RuntimeException


class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button.setOnClickListener {

//            throw RuntimeException("Test fail")


            throw RuntimeException("Test fail")
        }
    }
}
