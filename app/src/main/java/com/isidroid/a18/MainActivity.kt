package com.isidroid.a18

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val repository by lazy { LocationRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.start()

        btnRefreshed.setOnClickListener { refresh() }
        btnStart.setOnClickListener { viewmodel.start() }
        btnStop.setOnClickListener { stop() }
    }

    override fun onCreateViewModel() {
//        viewmodel.error.observe(this, Observer { Timber.e("Uosj $it") })
    }

    private fun refresh() {
        repository.start(
            useLast = false,
            onLocation = { Timber.i("onLocation $it") },
            onError = { Timber.e(it.message) }
        )
    }

    private fun stop() {
        repository.stop()
    }
}