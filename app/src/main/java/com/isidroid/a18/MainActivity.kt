package com.isidroid.a18

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.style.IconMarginSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import androidx.core.content.ContextCompat
import com.isidroid.utils.BaseActivity
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

        execute2()
    }

    private fun execute2() {
//        textview.movementMethod = LinkMovementMethod.getInstance()

        Timber.e("method=${textview.movementMethod}")

        val vectorDrawable: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_sample_printer)!!
        vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap =
                Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_4444)


        textview.text = YSpan(this)
                .append("Hello world")
                .style(RelativeSizeSpan(2f), TypefaceSpan("sans-serif-light"))
                .br()
//                .image(R.drawable.ic_sample_printer)
                .append(" ")
                .style(IconMarginSpan(bitmap, 10))

                .append("Some picture")
                .br()
                .append("I'm about to fall ")
                .append("asleep")
                .onclick { Timber.e("'$it'") }
                .build()


    }


}