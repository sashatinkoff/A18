package com.isidroid.pics.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.CallSuper
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.io.File

open class TakePictureViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var repository: TakePictureRepository
    private var takePictureRequest: TakePictureRequest? = null
    var data: HashMap<String, String>? = null
    val imageInfo = MutableLiveData<ImageInfo>()
    val error = MutableLiveData<Throwable>()

    fun create(context: Context) = apply {
        repository = TakePictureRepository(context, compositeDisposable)
    }

    @CallSuper
    open fun takePicture(
            caller: Any, data: HashMap<String, String>? = null,
            scale: Float? = null, maxSize: Int? = null
    ) {
        this.data = data
        val activity = caller as? Activity ?: (caller as Fragment).activity
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            takePictureRequest = TakePictureRequest(File.createTempFile("temp_image_", ".jpg"), scale, maxSize)

            val photoURI =
                    FileProvider.getUriForFile(activity!!, PictureConfig.get().authority, takePictureRequest!!.file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activity.startActivityForResult(intent, PictureConfig.get().codeTakePicture)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @CallSuper
    open fun pick(caller: Any, contentType: String, data: HashMap<String, String>? = null, isMultiple: Boolean = false) {
        this.data = data
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
            type = contentType
//            flags = FLAG_ACTIVITY_NO_HISTORY or FLAG_GRANT_READ_URI_PERMISSION
        }

        try {
            when (caller) {
                is Activity -> caller.startActivityForResult(intent, PictureConfig.get().codePick)
                is Fragment -> caller.startActivityForResult(intent, PictureConfig.get().codePick)
                else -> throw Exception("Can't pickGallery with this caller")
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    @CallSuper
    open fun pickGallery(caller: Any, isMultiple: Boolean = false, data: HashMap<String, String>? = null) =
            pick(caller, "image/*", data, isMultiple)

    @CallSuper
    open fun onResult(requestCode: Int, intent: Intent?) {
        val callback: (List<Result>?, Throwable?) -> Unit = { r, t ->
            if (t != null) error.postValue(t)
            else {
                imageInfo.postValue(ImageInfo(r, data))
            }

            doFinally()
        }

        if (requestCode == PictureConfig.get().codePick) {
            val uris = mutableListOf<Uri>()
            intent?.data?.let { uris.add(it) }

            val clipDataCount = intent?.clipData?.itemCount ?: 0
            (0 until clipDataCount).forEach {
                val uri = intent?.clipData?.getItemAt(it)?.uri
                if (uri != null) uris.add(uri)
            }

            repository.getFromGallery(uris.distinct(), callback)

        } else if (requestCode == PictureConfig.get().codeTakePicture && takePictureRequest != null)
            repository.processPhoto(takePictureRequest, callback)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    open fun doFinally(){}

    data class TakePictureRequest(val file: File, val scale: Float? = null, val maxSize: Int? = null)
    data class ImageInfo(val result: List<Result>?, val data: HashMap<String, String>?)
}