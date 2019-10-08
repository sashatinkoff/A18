package com.isidroid.a18

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.isidroid.a18.sample.rest.ApiTest
import com.isidroid.logger.Diagnostics
import com.isidroid.pics.PictureHandler
import com.isidroid.squareoffsdk.network.api.ApiSession
import com.isidroid.squareoffsdk.network.response.RegisterRequest
import com.isidroid.squareoffsdk.network.response.RegistrationRequest
import com.isidroid.utils.CoroutineViewModel
import kotlinx.coroutines.yield
import timber.log.Timber
import java.util.concurrent.CancellationException

class MainViewModel(application: Application) : CoroutineViewModel(application) {
    private val pictureHandler = PictureHandler(application)
    val intent = MutableLiveData<Intent>()
    val pictureResults = MutableLiveData<PictureHandler.PictureResults>()


    fun startLogging() {
        Diagnostics.instance.createLogger("test", "test")
    }

    fun stopLogging() {
        io {
            intent.postValue(Diagnostics.instance.logIntent(true))
        }
    }

    fun camera(activity: Activity) {
        pictureHandler.takePicture(activity)
    }

    fun gallery(activity: Activity) {
        pictureHandler.pickGallery(activity, decorIntent = { })
    }

    fun onResult(requestCode: Int, data: Intent?) {
        io {
            pictureResults.postValue(pictureHandler.result(requestCode, data))
        }
    }

    fun posts() = io {
        val response = ApiTest.create().posts().execute()
        Timber.i("sdfsdfdsf ${response.raw().request}")
    }

    fun works() = io {
        //        ApiSession.create().register(RegisterRequest(RegistrationRequest("sdf", "123", "123", "123", "tes"))).execute()
        ApiTest.create().posts().execute()

    }

}