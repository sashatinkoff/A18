package com.isidroid.a18

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.sample_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnSave.apply {
            text = "send"
            setOnClickListener { }
        }


        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

}