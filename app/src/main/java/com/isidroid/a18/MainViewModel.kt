package com.isidroid.a18

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.isidroid.utils.CoroutineViewModel

class MainViewModel(application: Application) : CoroutineViewModel(application) {

    enum class Action {
        NONE, INSERT, UPDATE
    }

    val action = MutableLiveData(Action.NONE)

    init {
    }

    fun save() {
    }

    fun read() {
    }

    fun update() {
    }
}