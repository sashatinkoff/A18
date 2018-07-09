package com.isidroid.a18

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.isidroid.utilsmodule.BaseActivity
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {
    var ts = System.currentTimeMillis()
    var time = 0L
    var dispose: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val publishSubject = PublishSubject.create<String>()
        dispose = publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .doOnNext {
                    Timber.i("on next t")
                }
                .subscribe {
                    time()
                    Timber.i("onTextChanged $time")
                }


        input.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                publishSubject.onNext(p0.toString())
            }
        })
    }

    fun time() {
        val now = System.currentTimeMillis()
        time = now - ts
        ts = now
    }
}
