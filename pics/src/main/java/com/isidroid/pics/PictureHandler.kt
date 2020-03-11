package com.isidroid.pics

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.provider.MediaStore
import androidx.annotation.CallSuper
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

class PictureHandler(
    app: Application,
    private val authority: String = "${app.packageName}.fileprovider",
    private val codeTakePicture: Int = 100,
    private val codePickGallery: Int = 101,
    private val debugCallback: ((String) -> Unit)? = null,
    var forceRotate: Boolean = true
) {
    private val repository =
        TakePictureRepository(
            context = app,
            forceRotate = forceRotate,
            debugCallback = debugCallback
        )

    private var takePictureRequest: TakePictureRequest? = null
    private var data: HashMap<String, String>? = null

    fun takePicture(
        caller: Any,
        data: HashMap<String, String>? = null,
        scale: Float? = null,
        maxSize: Int? = null
    ) {
        this.data = data
        val activity = caller as? Activity ?: (caller as Fragment).activity
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureRequest =
            TakePictureRequest(File.createTempFile("temp_image_", ".jpg"), scale, maxSize)

        val photoURI = FileProvider.getUriForFile(activity!!, authority, takePictureRequest!!.file)

        debugCallback?.invoke("takePicture, photiURI=$photoURI, request=$takePictureRequest")

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        when (caller) {
            is Activity -> caller.startActivityForResult(intent, codeTakePicture)
            is Fragment -> caller.startActivityForResult(intent, codeTakePicture)
        }
    }


    @CallSuper
    fun pickGallery(
        caller: Any,
        contentType: String,
        data: HashMap<String, String>? = null,
        isMultiple: Boolean = false,
        decorIntent: ((Intent) -> Unit)? = null
    ) {

        this.data = data
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .apply {
                flags = FLAG_GRANT_READ_URI_PERMISSION
                type = contentType

                addCategory(Intent.CATEGORY_OPENABLE)
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple)
                decorIntent?.invoke(this)
            }

        debugCallback?.invoke("pickGallery")

        when (caller) {
            is Activity -> caller.startActivityForResult(intent, codePickGallery)
            is Fragment -> caller.startActivityForResult(intent, codePickGallery)
            else -> throw Exception("Can't pickGallery with this caller")
        }
    }

    fun pickGallery(
        caller: Any,
        isMultiple: Boolean = false,
        data: HashMap<String, String>? = null,
        decorIntent: ((Intent) -> Unit)? = null
    ) =
        pickGallery(caller, "image/*", data, isMultiple, decorIntent)

    fun result(requestCode: Int, intent: Intent?): PictureResults? {
        val isPickRequest = requestCode == codePickGallery
        val isTakePictureRequest = requestCode == codeTakePicture

        debugCallback?.invoke("result requestCode=$requestCode, isPickRequest=$isPickRequest, isTakePictureRequest=$isTakePictureRequest")

        val result = when {
            isPickRequest -> repository.getFromGallery(intent)
            isTakePictureRequest -> listOf(repository.processPhoto(takePictureRequest))
            else -> null
        }


        val pictureResult = result?.let { PictureResults(it, data) }
        debugCallback?.invoke("result pictureResult=$pictureResult")

        return pictureResult
    }


    data class TakePictureRequest(
        val file: File,
        val scale: Float? = null,
        val maxSize: Int? = null
    )

    data class PictureResults(val list: List<ImageInfo>, val data: HashMap<String, String>?)
}