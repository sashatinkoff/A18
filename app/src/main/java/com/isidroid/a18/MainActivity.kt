package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.sample.rest.ApiTest
import com.isidroid.utils.BindActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        execute()
    }

    private fun execute() {
//        GlobalScope.launch(Dispatchers.Main) {
//            val one = async(Dispatchers.IO) { load(0) }
//            val two = async(Dispatchers.IO) { load(1) }
//
//            show(one.await(), two.await())
//        }

        GlobalScope.async {
            val post = load(0)
        }
    }

    private fun show(vararg data: Any) {
        val debug = data.mapTo(mutableListOf()) { it as ApiTest.PostResponse }
        Timber.i("show $debug on ${Thread.currentThread().name}")
    }

    private fun load(index: Int): Any {
        return ApiTest.create().posts().toFuture().get()[index]
    }

}