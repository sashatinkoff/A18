package com.isidroid.a18

import android.Manifest
import android.os.Bundle
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.utils.YSpan
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CompositePermissionListener()).check()


        btnStart.setOnClickListener { execute2() }

    }

    private fun execute2() {
//        textview.movementMethod = LinkMovementMethod.getInstance()
        Timber.e("method=${textview.movementMethod}")

        textview.text = YSpan(this)
                .append("Hello world")
                .style(RelativeSizeSpan(2f), TypefaceSpan("sans-serif-light"))
                .br()
                .image(R.drawable.ic_sample_printer)
                .append(" ")

                .append("Some picture")
                .br()
                .append("I'm about to fall ")
                .append("asleep")
                .onclick { Timber.e("'$it'") }
                .build()


    }


}