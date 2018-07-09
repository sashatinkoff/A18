package com.isidroid.a18.sample.viewmodels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.isidroid.a18.R
import com.isidroid.a18.core.Outcome
import com.isidroid.a18.sample.viewmodels.di.DaggerPostsComponent
import com.isidroid.a18.sample.viewmodels.di.PostsDH
import kotlinx.android.synthetic.main.activity_sample_posts.*
import javax.inject.Inject

class SamplePostsActivity : AppCompatActivity() {
    @Inject lateinit var viewModelFactory: PostsViewModelFactory

    private val viewModel: PostsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PostsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_posts)

        PostsDH.posts.inject(this)

        viewModel.data.observe(this, Observer {
            when (it) {
                is Outcome.Progress -> {
                    progressbar.visibility = if (it.loading) View.VISIBLE else View.INVISIBLE
                    if(it.loading) results.text = "Loading..."
                }
                is Outcome.Failure -> results.text = it.e.message
                is Outcome.Success -> results.text = "ok ${it.data.size}"
            }
        })

        button.setOnClickListener { viewModel.posts() }
    }
}
