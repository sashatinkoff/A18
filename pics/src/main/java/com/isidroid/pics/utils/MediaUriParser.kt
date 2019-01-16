package com.isidroid.pics.utils

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

class MediaUriParser {
    private var result: Result? = null
    private val context = PictureConfig.get().context

    private fun getColumn(cursor: Cursor, name: String) = cursor.getColumnIndex(name)

    fun parse(uri: Uri): Result? {
        var cursor: Cursor? = null
        when {
            MediaHelper.isGooglePhotosUri(uri) -> googlePhotos(uri)
            MediaHelper.isGoogleDrive(uri) -> googleDrive(uri)
            MediaHelper.isDownloadsDocument(uri) -> cursor = downloadsDocument(uri)
            MediaHelper.isExternalStorageDocument(uri) -> externalStorage(uri)
            MediaHelper.isMediaDocument(uri) -> cursor = media(uri)
        }

        cursor?.close()
        return result
    }

    @Throws(IOException::class)
    private fun googlePhotos(uri: Uri): Cursor? = googleDrive(uri)

    @Throws(IOException::class)
    private fun googleDrive(uri: Uri): Cursor? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val result = Result()

        val projection = arrayOf(
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA
        )


        val cursor = getData(uri, null, projection)
        var filename = UUID.randomUUID().toString().substring(0, 5)

        if (cursor?.moveToFirst() == true) {
            filename = cursor.getString(getColumn(cursor, projection[0]))
            result.dateTaken = Date(cursor.getLong(getColumn(cursor, projection[1])))
            result.localPath = cursor.getString(getColumn(cursor, projection[2]))
        }

        // the file is not stored locally, then download it to the device
        if (result.localPath == null) {
            try {
                val file = File(context.cacheDir, "$filename.jpg")
                inputStream.use { input -> file.outputStream().use { input.copyTo(it) } }
                result.localPath = file.absolutePath

            } catch (e: Exception) {
            } finally {
                closeStream(inputStream)
            }
        }

        this.result = result
        return cursor
    }

    private fun closeStream(inputStream: InputStream?) {
        try {
            inputStream?.close()
        } catch (e: Exception) {
        }
    }

    @Throws(Exception::class)
    private fun media(uri: Uri): Cursor? {
        var cursor: Cursor? = null

        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]
        var contentUri: Uri? = null
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DISPLAY_NAME)

        if ("image" == type)
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        if (contentUri != null)
            cursor = getData(contentUri, split[1], projection)

        if (cursor?.moveToFirst() == true) {
            result = Result().apply {
                localPath = cursor.getString(getColumn(cursor, projection[0]))
                dateTaken = Date(cursor.getLong(getColumn(cursor, projection[1])))
            }
        }

        return cursor
    }

    private fun externalStorage(uri: Uri) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        if ("primary".equals(type, ignoreCase = true)) {
            result = Result().apply { localPath = Environment.getExternalStorageDirectory().toString() + "/" + split[1] }
        }
    }

    private fun downloadsDocument(uri: Uri): Cursor? {
        val cursor: Cursor?

        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

        val projection = arrayOf("_data", "lastmod")
        cursor = getData(contentUri, id, projection)
        if (cursor?.moveToFirst() == true) {
            result = Result()
            result?.localPath = cursor.getString(getColumn(cursor, projection[0]))
            result?.dateTaken = Date(cursor.getLong(getColumn(cursor, projection[1])))
        }

        return cursor
    }

    private fun getData(uri: Uri, id: String?, projection: Array<String>): Cursor? {
        val selection = "_id=?"
        val selectionArgs = arrayOf(id ?: "")
        return context.contentResolver.query(uri, projection, selection, selectionArgs, null)
    }

}