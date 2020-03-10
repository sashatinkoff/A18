package com.isidroid.a18.ui

import android.os.Bundle
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.sample.rest.ApiReddit
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnStart.setOnClickListener { start() }

    }

    private fun start() {
        Tasks.io(
            doBefore = { },
            doWork = {
                val response = ApiReddit.create().posts("isidroid").execute()
                val info = response.body()?.data?.children?.map { it.data }

                Timber.i("sdfsdfs code=${response.code()}, $info")
            },
            onComplete = { },
            onError = { Timber.e("sdfsdfs ${it.message}") }
        )
    }

}