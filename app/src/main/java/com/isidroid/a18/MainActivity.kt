package com.isidroid.a18

import android.os.Bundle
import com.isidroid.utils.BaseActivity
import android.speech.tts.TextToSpeech
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity(), TextToSpeech.OnInitListener {
    override fun onInit(p0: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

