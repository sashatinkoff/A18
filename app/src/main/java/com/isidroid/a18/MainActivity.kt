package com.isidroid.a18

import android.graphics.Bitmap
import android.os.Bundle
import com.isidroid.utils.BaseActivity
import com.isidroid.utils.addTo
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener { execute() }
    }

    private fun execute() {
        val data = arrayListOf(1, 200, 400, 50000)
        val updated = mutableListOf("Some string")
        val upload = Flowable.just("COMPLETE ACTION")

        Flowable.concat(Flowable.fromIterable(data), Flowable.fromIterable(updated), upload)
                .map { param ->
                    var result = ""

                    (param as? Int)?.apply {
                        val bitmap = Bitmap.createBitmap(param, param, Bitmap.Config.ARGB_8888)
                        result = "create bitmap " +
//                                "[${bitmap.width}, ${bitmap.height}]" +
                                ""

                    }

                    (param as? String)?.apply { result = param }
                    result
                }
                .subscribe({ Timber.i("next $it") }, { Timber.e(it) }, { Timber.e("complete") })
                .addTo(CompositeDisposable())
    }
}