package com.isidroid.a18

import android.content.Intent
import android.os.Bundle
import com.isidroid.a18.sample.viewmodels.SamplePostsActivity
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, SamplePostsActivity::class.java))
    }
}
