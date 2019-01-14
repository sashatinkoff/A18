package com.isidroid.pics.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import com.isidroid.pics.addTo
import com.isidroid.pics.subscribeIoMain
import com.isidroid.pics.utils.BitmapUtils
import com.isidroid.pics.utils.FileUtils
import com.isidroid.pics.utils.MediaUriParser
import com.isidroid.pics.utils.ImageHeaderParser
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import java.util.*

class TakePictureRepository(private val compositeDisposable: CompositeDisposable) {
    fun getFromGallery(uri: Uri?, callback: (Result?, Throwable?) -> Unit) {
        if (uri == null) {
            callback(null, Throwable("Uri is null"))
            return
        }

        Flowable.just(uri)
                .map { u ->
                    try {
                        MediaUriParser().parse(u)
                    } catch (e: Exception) {
                        val file = File(FileUtils.getPath(PictureConfig.get().context, uri))

                        Result().apply {
                            localPath = file.absolutePath
                            bitmap = if (file.exists()) BitmapFactory.decodeFile(localPath) else null
                        }
                    }
                }
                .doOnNext { result ->
                    val exif = ExifInterface(result?.localPath)
                    val ori0 = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
                    rotate(result)

                    Log.e("nativeHeapSize", "orient=$ori0, or2=${result?.orientation}")
                }
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
                    result.bitmap = BitmapUtils.decodeFile(it.file.absolutePath)
                    val scale = it.scale ?: 0f
                    val maxSize = it.maxSize ?: 0
                    val bitmap = result.bitmap!!

                    // let's scale it
                    if (scale > 0f && scale < 1f)
                        result.bitmap = BitmapUtils.scalePhoto(bitmap, scale)
                    else if (maxSize > 0 && maxSize < Math.max(bitmap.width, bitmap.height))
                        result.bitmap = BitmapUtils.scalePhoto(bitmap, maxSize)

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
        if (result?.bitmap == null) return
        val file = File(result.localPath)
        if (file.exists()) {
            val orientation = ImageHeaderParser(file.inputStream()).orientation
            result.orientation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }

            try {
                val orientationMatrix = Matrix()
                orientationMatrix.postRotate(result.orientation.toFloat())

                result.bitmap = Bitmap.createBitmap(result.bitmap, 0, 0,
                        result.bitmap?.width ?: 0, result.bitmap?.height
                        ?: 0, orientationMatrix, false)

                val out = FileOutputStream(file)
                result.bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.close()
            } catch (e: Exception) {
            }
        }
    }


}