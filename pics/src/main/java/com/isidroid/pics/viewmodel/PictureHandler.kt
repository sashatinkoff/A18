package com.isidroid.pics.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.provider.MediaStore
import androidx.annotation.CallSuper
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.isidroid.pics.ImageInfo
import java.io.File

class PictureHandler(app: Application,
                     private val authority: String = "${app.packageName}.fileprovider",
                     private val codeTakePicture: Int = 100,
                     private val codePickGallery: Int = 101) {
    private val repository = TakePictureRepository(app)
    private var takePictureRequest: TakePictureRequest? = null
    private var data: HashMap<String, String>? = null

    fun takePicture(caller: Any, data: HashMap<String, String>? = null, scale: Float? = null, maxSize: Int? = null) {
        this.data = data
        val activity = caller as? Activity ?: (caller as Fragment).activity
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureRequest = TakePictureRequest(File.createTempFile("temp_image_", ".jpg"), scale, maxSize)

        val photoURI = FileProvider.getUriForFile(activity!!, authority, takePictureRequest!!.file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        when (caller) {
            is Activity -> caller.startActivityForResult(intent, codeTakePicture)
            is Fragment -> caller.startActivityForResult(intent, codeTakePicture)
        }
    }


    @CallSuper
    fun pickGallery(caller: Any, contentType: String,
                    data: HashMap<String, String>? = null,
                    isMultiple: Boolean = false,
                    onIntent: ((Intent) -> Unit)? = null) {

        this.data = data
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .apply {
                    flags = FLAG_GRANT_READ_URI_PERMISSION
                    type = contentType

                    addCategory(Intent.CATEGORY_OPENABLE)
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
                    onIntent?.invoke(this)
                }

        when (caller) {
            is Activity -> caller.startActivityForResult(intent, codePickGallery)
            is Fragment -> caller.startActivityForResult(intent, codePickGallery)
            else -> throw Exception("Can't pickGallery with this caller")
        }
    }

    fun pickGallery(caller: Any,
                    isMultiple: Boolean = false,
                    data: HashMap<String, String>? = null,
                    decorIntent: ((Intent) -> Unit)? = null) =
            pickGallery(caller, "image/*", data, isMultiple, decorIntent)

    fun result(requestCode: Int, intent: Intent?): PictureResults? {
        val isPickRequest = requestCode == codePickGallery
        val isTakePictureRequest = requestCode == codeTakePicture

        val result = when {
            isPickRequest -> repository.getFromGallery(intent)
            isTakePictureRequest -> listOf(repository.processPhoto(takePictureRequest))
            else -> null
        }


        return result?.let { PictureResults(it, data) }
    }


    data class TakePictureRequest(val file: File, val scale: Float? = null, val maxSize: Int? = null)
    data class PictureResults(val list: List<ImageInfo>, val data: HashMap<String, String>?)
}