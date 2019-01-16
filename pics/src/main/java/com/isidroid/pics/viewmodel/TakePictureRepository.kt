package com.isidroid.pics.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import com.isidroid.pics.addTo
import com.isidroid.pics.subscribeIoMain
import com.isidroid.pics.utils.FileUtils
import com.isidroid.pics.utils.ImageHeaderParser
import com.isidroid.pics.utils.MediaUriParser
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.FileOutputStream
import java.util.*

class TakePictureRepository(private val compositeDisposable: CompositeDisposable) {
    fun getFromGallery(uri: Uri?, callback: (Result?, Throwable?) -> Unit) {
        if (uri == null) {
            callback(null, Throwable("Uri is null"))
            return
        }

        Flowable.just(uri)
                .map { u -> MediaUriParser(PictureConfig.get().context).parse(u) }
                .doOnNext { rotate(it) }
                .subscribeIoMain()
                .subscribe(
                        { callback(it, null) },
                        { callback(null, it) }
                )
                .addTo(compositeDisposable)
    }

    fun processPhoto(request: TakePictureViewModel.TakePictureRequest?, callback: (Result?, Throwable?) -> Unit) {
        if (request?.file == null) {
            callback(null, Throwable("Uri is null"))
            return
        }

        Flowable.just(request)
                .map {
                    val result = Result()
                    result.localPath = request.file.absolutePath
                    result.dateTaken = Date()

                    rotate(result)
                    result
                }
                .subscribe(
                        { callback(it, null) },
                        { callback(null, it) }
                )
                .addTo(compositeDisposable)
    }

    private fun rotate(result: Result?) {
        if (result?.localPath == null) return

        val file = File(result.localPath)
        if (file.exists()) {
            val orientation = ImageHeaderParser(file.inputStream()).orientation
            result.orientation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            var bitmap: Bitmap? = null
            var fileOutputStream: FileOutputStream? = null


            try {
                val orientationMatrix = Matrix()
                orientationMatrix.postRotate(result.orientation.toFloat())

                bitmap = BitmapFactory.decodeFile(result.localPath)

                fileOutputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.close()
            } catch (e: Exception) {
            }
        }
    }


}