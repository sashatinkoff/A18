package com.isidroid.pics

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ExifInterface.ORIENTATION_TRANSVERSE
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class TakePictureRepository(
    private val context: Context,
    val forceRotate: Boolean = true,
    private val debugCallback: ((String) -> Unit)? = null
) {
    fun getFromGallery(intent: Intent?): List<ImageInfo> {
        val uris = mutableListOf<Uri>()
        intent?.data?.let { uris.add(it) }

        val clipDataCount = intent?.clipData?.itemCount ?: 0
        (0 until clipDataCount).forEach {
            val uri = intent?.clipData?.getItemAt(it)?.uri
            if (uri != null) uris.add(uri)
        }

        debugCallback?.invoke("getFromGallery uris=${uris.distinct().size}, forceRotate=$forceRotate")

        return getFromGallery(uris.distinct())
    }

    fun getFromGallery(uris: List<Uri>?): List<ImageInfo> {
        check(!uris.isNullOrEmpty()) {
            debugCallback?.invoke("getFromGallery error occurred: Uris are null")
            "Uris are null"
        }

        return uris.mapNotNull {
            debugCallback?.invoke("getFromGallery uri=$it")

            MediaUriParser(context, debugCallback = debugCallback).parse(it)
                ?.apply {
                    debugCallback?.invoke("getFromGallery parse file $localPath, isImage=${isImage()}")
                    if (isImage()) localPath = rotate(this)
                }
        }
    }

    fun processPhoto(request: PictureHandler.TakePictureRequest?): ImageInfo {
        if (request?.file?.exists() != true) throw IllegalStateException("File is null")

        return ImageInfo().apply {
            localPath = request.file.absolutePath
            dateTaken = Date()
            localPath = rotate(this)
        }
    }

    fun rotate(imageInfo: ImageInfo?): String? {
        if (imageInfo?.localPath == null) return null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(imageInfo.localPath, options)

        if (options.outWidth < 0 || options.outHeight < 0) {
            debugCallback?.invoke("File is not valid bitmap")
            throw Exception("File is not valid bitmap")
        }

        if (!forceRotate) return imageInfo.localPath

        val file = File(imageInfo.localPath)
        if (!file.exists()) {
            debugCallback?.invoke("File not found when trying to rotate it")
            throw Exception("File not found")
        }

        var fileStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var bitmap: Bitmap? = null
        var path = file.absolutePath
        var exception: Throwable? = null

        try {
            fileStream = file.inputStream()

            val orientation = ImageHeaderParser(fileStream).orientation
            imageInfo.orientation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ORIENTATION_TRANSVERSE -> 270
                else -> 0
            }

            debugCallback?.invoke("rotate file ${imageInfo.localPath}, orientation=$orientation, angle=${imageInfo.orientation}")

            if (orientation >= 0) {
                val orientationMatrix = Matrix()
                orientationMatrix.postRotate(imageInfo.orientation.toFloat())

                val rotatedFile = File(
                    context.cacheDir,
                    UUID.randomUUID().toString().substring(0, 5) + ".jpg"
                )
                bitmap = BitmapFactory.decodeFile(imageInfo.localPath)

                debugCallback?.invoke("rotate original=[${bitmap.width}, ${bitmap.height}]")

                bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    orientationMatrix,
                    false
                )

                debugCallback?.invoke("rotate modified bitmap=[${bitmap.width}, ${bitmap.height}]")

                fileOutputStream = FileOutputStream(rotatedFile)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

                debugCallback?.invoke("rotate saved file=${rotatedFile.absolutePath}, exists=${rotatedFile.exists()}")

                path = rotatedFile.absolutePath
            }
        } catch (e: Exception) {
            debugCallback?.invoke("rotate error occurred ${e.message}")
            exception = e
        } finally {
            wrapTry { bitmap?.recycle() }
            wrapTry { fileStream?.close() }
            wrapTry { fileOutputStream?.close() }
        }

        exception?.let { throw it }

        return path
    }

    private fun wrapTry(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}