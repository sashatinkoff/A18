package com.isidroid.pics.viewmodel

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import io.reactivex.disposables.CompositeDisposable
import java.io.File

open class TakePictureViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository = TakePictureRepository(compositeDisposable)
    private var takePictureRequest: TakePictureRequest? = null
    var data: HashMap<String, String>? = null
    val imageInfo = MutableLiveData<ImageInfo>()
    val error = MutableLiveData<Throwable>()

    fun takePicture(caller: Any, data: HashMap<String, String>? = null,
                    scale: Float? = null, maxSize: Int? = null) {
        this.data = data
        val activity = caller as? Activity ?: (caller as Fragment).activity
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureRequest = TakePictureRequest(File.createTempFile("temp_image_", ".jpg"), scale, maxSize)

            val photoURI = FileProvider.getUriForFile(activity!!, PictureConfig.get().authority, takePictureRequest!!.file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activity.startActivityForResult(intent, PictureConfig.get().codeTakePicture)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pick(caller: Any, contentType: String, data: HashMap<String, String>? = null) {
        this.data = data
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = contentType
            flags = FLAG_ACTIVITY_NO_HISTORY or FLAG_GRANT_READ_URI_PERMISSION
        }

        try {
            when (caller) {
                is Activity -> caller.startActivityForResult(intent, PictureConfig.get().codePick)
                is Fragment -> caller.startActivityForResult(intent, PictureConfig.get().codePick)
                else -> throw Exception("Can't pickGallery with this caller")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pickGallery(caller: Any, data: HashMap<String, String>? = null) = pick(caller, "image/*", data)

    fun onResult(requestCode: Int, intent: Intent?) {
        val callback: (Result?, Throwable?) -> Unit = { r, t ->
            if (t != null) error.postValue(t)
            else imageInfo.postValue(ImageInfo(r, data))
            if (r?.localPath != null) onImageReady(r, data)
        }

        if (intent?.data != null && requestCode == PictureConfig.get().codePick)
            repository.getFromGallery(intent.data, callback)
        else if (requestCode == PictureConfig.get().codeTakePicture && takePictureRequest != null)
            repository.processPhoto(takePictureRequest, callback)
    }

    protected open fun onImageReady(result: Result, data: HashMap<String, String>?) {}

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    data class TakePictureRequest(val file: File, val scale: Float? = null, val maxSize: Int? = null)
    data class ImageInfo(val result: Result?, val data: HashMap<String, String>?)
}