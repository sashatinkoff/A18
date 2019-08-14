package com.isidroid.a18

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.isidroid.logger.Diagnostics
import com.isidroid.utils.CoroutineViewModel

class MainViewModel(application: Application) : CoroutineViewModel(application) {
    val intent = MutableLiveData<Intent>()

    fun startLogging() {
        Diagnostics.instance.createLogger("test", "test")
    }

    fun stopLogging() {
        io {
            intent.postValue(Diagnostics.instance.logIntent(getApplication(), true))
        }
    }

}