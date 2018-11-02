package com.isidroid.pics

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import java.io.File

open class TakePictureViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository = TakePictureRepository(compositeDisposable)
    private var takePictureRequest: TakePictureRequest? = null
    val imageInfo = MutableLiveData<ImageInfo>()

    fun takePicture(caller: Any, scale: Float? = null, maxSize: Int? = null) {
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

    fun pickGallery(caller: Any) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"

        try {
            when (caller) {
                is Activity -> caller.startActivityForResult(intent, PictureConfig.get().codePickPicture)
                is Fragment -> caller.startActivityForResult(intent, PictureConfig.get().codePickPicture)
                else -> throw Exception("Can't pickGallery with this caller")
            }
        } catch (e: Exception) {
        }
    }

    fun onResult(requestCode: Int, intent: Intent?) {
        val callback: (Result?, Throwable?) -> Unit = { r, t ->
            imageInfo.postValue(ImageInfo(r, t))
            if(r?.localPath != null) onImageReady(r)
        }

        if (intent?.data != null && requestCode == PictureConfig.get().codePickPicture)
            repository.getFromGallery(intent.data, callback)
        else if (requestCode == PictureConfig.get().codeTakePicture && takePictureRequest != null)
            repository.processPhoto(takePictureRequest, callback)
    }

    protected open fun onImageReady(result: Result) {}

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    data class TakePictureRequest(val file: File, val scale: Float? = null, val maxSize: Int? = null)
    data class ImageInfo(val result: Result?, val throwable: Throwable? = null)
}