package com.isidroid.a18.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isidroid.a18.R
import com.isidroid.loggermodule.Diagnostics.Companion.LOGTAG
import com.isidroid.utilsmodule.YRealm
import timber.log.Timber

class SamplePostsActivity : AppCompatActivity() {


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_posts)

        val person = YRealm.fromJson(intent.getStringExtra("person"), Person::class.java)

        Timber.tag(LOGTAG).i("SamplePostsActivity $person")
    }

}
