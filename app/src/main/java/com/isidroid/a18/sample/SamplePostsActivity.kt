package com.isidroid.a18.sample

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.isidroid.a18.R
import com.isidroid.utilsmodule.BaseActivity
import kotlinx.android.synthetic.main.activity_sample_posts.*

class SamplePostsActivity : BaseActivity() {


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_posts)
        supportPostponeEnterTransition()

        results.text = intent.getStringExtra(ARG_TEXT)
        Glide.with(imageview)
                .load(intent.getStringExtra(ARG_IMAGE))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageview.setImageDrawable(resource)
                        supportStartPostponedEnterTransition()
                    }
                })
    }

    companion object {
        const val ARG_TEXT = "ARG_TEXT"
        const val ARG_IMAGE = "ARG_IMAGE"
        const val ARG_SMALL = "ARG_SMALL"

    }
}
