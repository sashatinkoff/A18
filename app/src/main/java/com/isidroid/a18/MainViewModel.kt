package com.isidroid.a18

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.isidroid.logger.Diagnostics
import com.isidroid.pics.viewmodel.PictureHandler
import com.isidroid.utils.CoroutineViewModel

class MainViewModel(application: Application) : CoroutineViewModel(application) {
    private val pictureHandler = PictureHandler(application)
    val intent = MutableLiveData<Intent>()
    val pictureResults = MutableLiveData<PictureHandler.PictureResults>()


    fun startLogging() {
        Diagnostics.instance.createLogger("test", "test")
    }

    fun stopLogging() {
        io {
            intent.postValue(Diagnostics.instance.logIntent(getApplication(), true))
        }
    }

    fun camera(activity: Activity) {
        pictureHandler.takePicture(activity)
    }

    fun gallery(activity: Activity) {
        pictureHandler.pickGallery(activity)
    }

    fun onResult(requestCode: Int, data: Intent?) {
        io {
            pictureResults.postValue(pictureHandler.result(requestCode, data))
        }
    }

}