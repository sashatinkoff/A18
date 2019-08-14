package com.isidroid.a18

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.sample_main.*


class MainActivity : BindActivity<ActivityMainBinding>() {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnSave.apply {
            text = "Start logging"
            setOnClickListener { viewmodel.startLogging() }
        }

        btnPdf.apply {
            text = "Stop logging"
            setOnClickListener { viewmodel.stopLogging() }
        }
    }


    override fun onCreateViewModel() {
        viewmodel.intent.observe(this, Observer { startActivity(it) })
    }
}