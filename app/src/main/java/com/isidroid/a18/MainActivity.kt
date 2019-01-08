package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.queues.QueueManager
import com.isidroid.utils.BindActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    private val manager = QueueManager<String>(CompositeDisposable())
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        btnSubmit.setOnClickListener {
//            counter++
//            manager.add(Status(counter))
//        }
//
//        btnCancel.setOnClickListener {
//            manager.cancel(counter - 1)
//        }
    }

}