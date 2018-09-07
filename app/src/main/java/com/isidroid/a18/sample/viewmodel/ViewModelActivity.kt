package com.isidroid.a18.sample.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.isidroid.a18.R

class ViewModelActivity : AppCompatActivity() {
    private lateinit var viewmodel: ScoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity_view_model)

         viewmodel = ViewModelProviders.of(this).get(ScoreViewModel::class.java)
    }
}
