package com.isidroid.a18.ui

import android.os.Bundle
import com.isidroid.a18.R
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import kotlin.random.Random

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnStart.setOnClickListener { start() }
    }

    private fun start() {
        Tasks.io(
            doBefore = { onState(State.Loading) },
            doWork = {
                Thread.sleep(1_000)
                val isError = Random(10).nextBoolean()
//                if (isError) throw Exception("Not success")
                Pet(name = "Dog", age = Random(1).nextInt(1, 10))
            },
            onComplete = { onState(State.Data(pet = it!!)) },
            onError = { onState(State.Error(message = it.message ?: "Not available")) }
        )
    }

    private fun onState(state: State) {
        val text = when (state) {
            is State.Error -> "error: ${state.message}"
            State.Loading -> "loading"
            is State.Data -> "Data is ready ${state.pet}"
        }

        Timber.i("sdfsdfsdf onState $state $text")
    }
}