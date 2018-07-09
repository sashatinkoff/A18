package com.isidroid.a18

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.isidroid.a18.sample.viewmodels.Outcome
import com.isidroid.a18.sample.viewmodels.PostsViewModel
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity(), LifecycleObserver {
    private val viewModel: PostsViewModel by lazy {
        ViewModelProviders.of(this).get(PostsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(this)

        viewModel.data.observe(this, Observer {
            Timber.i("update $it")
            when (it) {
                is Outcome.Progress -> progressbar.visibility = if (it.loading) View.VISIBLE else View.GONE
                is Outcome.Failure -> Timber.e("error=${it.e}")
                is Outcome.Success -> Timber.i("ok ${it.data.size}")
            }
        })

        backgroundSwitcher.setOnClickListener { viewModel.posts() }
    }
}
