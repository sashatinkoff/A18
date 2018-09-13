package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.backdrop.Backdrop2
import com.isidroid.utils.BaseActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var backdrop: Backdrop2
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        backdrop = Backdrop2(container, backdropContainer)

        button.setOnClickListener {
            when (counter) {
                0 -> backdrop.show()
                1 -> backdrop.hide()
                2 -> backdrop.show()
                else -> backdrop.toggle()
            }
            counter++
        }

        destroyButton.setOnClickListener { backdrop.destroy() }
    }


    private fun showBackdrop() {
//        val view = LayoutInflater.from(this).inflate(R.layout.sample_backdrop, backdropContainer)
//        backdrop.show()
//
//        Flowable.range(1, 30)
//                .doOnNext {
//                    Thread.sleep(100)
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    val chipView = Chip(this).apply { text = UUID.randomUUID().toString().substring(0, 5) }
//                    view.chipGroup.addView(chipView)
//                }
//                .addTo(compositeDisposable)
    }

//    override fun onBackPressed() {
//        if (backdrop.isShown) {
//            compositeDisposable.clear()
//            backdrop.destroy()
//        } else super.onBackPressed()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        backdrop.destroy()
//    }
}