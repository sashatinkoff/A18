package com.isidroid.a18

import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.isidroid.a18.backdrop.*
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.addTo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*


class MainActivity : BaseActivity() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var backdrop: Backdrop2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        backdrop = Backdrop2(container, backdropContainer)
                .onCollapse { log("onCollapse") }
                .onExpand { log("onExpand") }
                .onCollapseStarted { log("onCollapseStarted") }
                .onExpandStarted { log("onExpandStarted") }
                .onCollapseDone { log("onCollapseDone") }
                .onExpandDone { log("onExpandDone") }
                .onDestroy { log("onDestroy") }

        button.setOnClickListener {
            backdrop.expand()
        }

        loadContentButton.setOnClickListener { loadContent() }

        destroyButton.setOnClickListener { backdrop.destroy() }
    }

    private fun log(msg: String) {
        Timber.tag("Backdrop").i(msg)
    }

    private fun loadContent() {
        backdrop
                .withView(LayoutInflater.from(this).inflate(R.layout.sample_backdrop, backdropContainer, false))
                .expand()

        Flowable.range(1, 30)
                .doOnNext { Thread.sleep(100) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val chipView = Chip(this).apply { text = UUID.randomUUID().toString().substring(0, 5) }
                    backdrop.view()?.findViewById<ChipGroup>(R.id.chipGroup)?.addView(chipView)
                }
                .addTo(compositeDisposable)
    }


    override fun onBackPressed() {
        if (backdrop.isExpanded()) {
            compositeDisposable.clear()
//            backdrop.destroy()
            backdrop.collapse()
        } else super.onBackPressed()
    }
//
//    override fun onPause() {
//        super.onPause()
//        backdrop.destroy()
//    }
}