package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.queues.QueueManager
import com.isidroid.utils.BindActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    private val manager = QueueManager<Int>(CompositeDisposable())
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        manager
                .maxAttempts(2)
                .onError { Timber.e("QueueManager ${it.message}") }
                .onProgress { item, progress, total -> Timber.i("$item=$progress/$total") }
                .onExecute {
                    Thread.sleep(1000)
//                    if (it == 2) QueueManager.fail()
//                    else
                    QueueManager.success()
                }
                .create()



        btnSubmit.setOnClickListener {
            counter++
//            Timber.i("QueueManager add $counter")
            manager.add(counter)
        }

        btnCancel.setOnClickListener { manager.cancel(counter - 1) }
        btnClear.setOnClickListener {
            counter = 0
            manager.destroy().create()
        }
    }

}