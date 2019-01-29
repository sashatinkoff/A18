package com.isidroid.pics.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.webkit.MimeTypeMap
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import com.isidroid.pics.addTo
import com.isidroid.pics.subscribeIoMain
import com.isidroid.pics.utils.BitmapUtils
import com.isidroid.pics.utils.ImageHeaderParser
import com.isidroid.pics.utils.MediaUriParser
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class TakePictureRepository(private val compositeDisposable: CompositeDisposable) {
    private val context = PictureConfig.get().context

    fun getFromGallery(uris: List<Uri>?, callback: (List<Result>?, Throwable?) -> Unit) {
        if (uris?.isNullOrEmpty() == true) {
            callback(null, Throwable("Uris null"))
            return
        }

        val result = mutableListOf<Result?>()
        Flowable.fromIterable(uris)
            .map { u ->
                MediaUriParser(context).parse(u)?.apply {
                    if (BitmapUtils.isImage(localPath)) localPath = rotate(this)
                }
            }
            .subscribeIoMain()
            .subscribe(
                { result.add(it) },
                {
                    Timber.e(it)
                    callback(null, it)
                },
                {
                    val completeResult = mutableListOf<Result>()
                    result.filter { it != null }.forEach { completeResult.add(it!!) }
                    callback(completeResult, null)
                }
            )
            .addTo(compositeDisposable)
    }

    fun processPhoto(request: TakePictureViewModel.TakePictureRequest?, callback: (List<Result>?, Throwable?) -> Unit) {
        if (request?.file == null) {
            callback(null, Throwable("Uri is null"))
            return
        }

        Flowable.just(request)
            .map {
                val result = Result()
                result.localPath = request.file.absolutePath
                result.dateTaken = Date()

                result.localPath = rotate(result)
                result
            }
            .subscribe(
                { callback(listOf(it), null) },
                { callback(null, it) }
            )
            .addTo(compositeDisposable)
    }

    private fun rotate(result: Result?): String? {
        if (result?.localPath == null) return null

        val file = File(result.localPath)
        var fileStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var bitmap: Bitmap? = null
        var path = file.absolutePath

        try {
            if (file.exists()) {
                if (file.exists()) {
                    fileStream = file.inputStream()

                    val orientation = ImageHeaderParser(fileStream).orientation
                    result.orientation = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270
                        else -> 0
                    }

                    val orientationMatrix = Matrix()
                    orientationMatrix.postRotate(result.orientation.toFloat())

                    val rotatedFile = File(file.parentFile, UUID.randomUUID().toString().substring(0, 5) + ".jpg")
                    bitmap = BitmapFactory.decodeFile(result.localPath)
                    fileOutputStream = FileOutputStream(rotatedFile)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

                    path = rotatedFile.absolutePath
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            wrapTry { bitmap?.recycle() }
            wrapTry { fileStream?.close() }
            wrapTry { fileOutputStream?.close() }
        }

        return path
    }

    private fun wrapTry(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
        }
    }
}