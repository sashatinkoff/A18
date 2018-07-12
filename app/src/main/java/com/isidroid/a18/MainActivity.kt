package com.isidroid.a18

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.isidroid.a18.sample.SamplePostsActivity
import com.isidroid.a18.sample.SamplePostsActivity.Companion.ARG_IMAGE
import com.isidroid.a18.sample.SamplePostsActivity.Companion.ARG_SMALL
import com.isidroid.a18.sample.SamplePostsActivity.Companion.ARG_TEXT
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth


class MainActivity : BaseActivity() {
    private val small = "https://i.redditmedia.com/231klF-0ULqETxRvq0gx61DWnsm5sE1tS7SVWZbDgLk.png?fit=crop&crop=faces%2Centropy&arh=2&w=108&s=5067eb7fc1771c87ff2f5a4ce078c4ff"
    private val full = "https://pp.userapi.com/c834200/v834200039/178b97/AQojoR-WLx0.jpg"

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Flowable.just("")
                .doOnNext { Glide.get(this).clearDiskCache() }
                .subscribeOn(Schedulers.io())
                .subscribe()

        Glide.with(imageview)
                .load(full)
                .into(imageview)

        results.text = UUID.randomUUID().toString()
        button.setOnClickListener { open() }
    }

    private fun open() {
        val intent = Intent(this, SamplePostsActivity::class.java).apply {
            putExtra(ARG_TEXT, results.text)
            putExtra(ARG_IMAGE, full)
            putExtra(ARG_SMALL, small)
        }

        val p1: androidx.core.util.Pair<View, String> = androidx.core.util.Pair.create(results, "profile")
        val p2: androidx.core.util.Pair<View, String> = androidx.core.util.Pair.create(imageview, "imageview")

        var options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2)
        options = ActivityOptionsCompat.makeBasic()

//        options = ActivityOptionsCompat.makeClipRevealAnimation(
//                imageview, 0, 0, imageview.width, imageview.height)

//        options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(imageview, (imageview.drawable as BitmapDrawable).bitmap, 0, 0)


        startActivity(intent, options.toBundle())
    }
}
