package com.isidroid.pics

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class TakePictureRepository(private val context: Context) {
    fun getFromGallery(intent: Intent?): List<ImageInfo> {
        val uris = mutableListOf<Uri>()
        intent?.data?.let { uris.add(it) }

        val clipDataCount = intent?.clipData?.itemCount ?: 0
        (0 until clipDataCount).forEach {
            val uri = intent?.clipData?.getItemAt(it)?.uri
            if (uri != null) uris.add(uri)
        }

        return getFromGallery(uris.distinct())
    }

    fun getFromGallery(uris: List<Uri>?): List<ImageInfo> {
        if (uris?.isNullOrEmpty() == true) throw IllegalStateException("Uris are null")

        return uris
            .mapTo(mutableListOf()) {
                MediaUriParser(context).parse(it)?.apply {
                    if (isImage()) localPath = rotate(this)
                }
            }.filterNotNull()

    }

    fun processPhoto(request: PictureHandler.TakePictureRequest?): ImageInfo {
        if (request?.file == null) throw IllegalStateException("File is null")

        return ImageInfo().apply {
            localPath = request.file.absolutePath
            dateTaken = Date()
            localPath = rotate(this)
        }
    }

    private fun rotate(imageInfo: ImageInfo?): String? {
        if (imageInfo?.localPath == null) return null

        val file = File(imageInfo.localPath)
        var fileStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var bitmap: Bitmap? = null
        var path = file.absolutePath

        try {
            if (file.exists()) {
                fileStream = file.inputStream()

                val orientation = ImageHeaderParser(fileStream).orientation
                imageInfo.orientation = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    1 -> 270
                    else -> 0
                }

                if (orientation >= 0) {
                    val orientationMatrix = Matrix()
                    orientationMatrix.postRotate(imageInfo.orientation.toFloat())

                    val rotatedFile = File(context.cacheDir, UUID.randomUUID().toString().substring(0, 5) + ".jpg")
                    bitmap = BitmapFactory.decodeFile(imageInfo.localPath)
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, orientationMatrix, false)
                    fileOutputStream = FileOutputStream(rotatedFile)
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

                    path = rotatedFile.absolutePath
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
            e.printStackTrace()
        }
    }
}