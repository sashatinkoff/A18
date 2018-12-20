package com.isidroid.pics.utils

import android.content.ContentUris
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import com.isidroid.pics.PictureConfig
import com.isidroid.pics.Result
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class MediaUriParser {
    private var result: Result? = null
    private val context = PictureConfig.get().context

    fun parse(uri: Uri): Result? {
        var cursor: Cursor? = null
        when {
            MediaHelper.isGooglePhotosUri(uri) -> googlePhotos(uri)
            MediaHelper.isDownloadsDocument(uri) -> cursor = downloadsDocument(uri)
            MediaHelper.isExternalStorageDocument(uri) -> externalStorage(uri)
            MediaHelper.isMediaDocument(uri) -> cursor = media(uri)
        }

        cursor?.close()
        return result
    }

    @Throws(IOException::class)
    private fun googlePhotos(uri: Uri): Cursor? {
        var inputStream = context.contentResolver.openInputStream(uri)
        result = Result()
        //            result.bitmap = BitmapUtils.INSTANCE.decodeStream(is);

        if (inputStream != null)
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()

            }

        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DISPLAY_NAME)
        val cursor = getData(uri, null, projection)
        if (cursor?.moveToFirst() == true) {
            result?.localPath = cursor.getString(getColumn(cursor, projection[0]))
            result?.dateTaken = Date(cursor.getLong(getColumn(cursor, projection[1])))
        }

        if (TextUtils.isEmpty(result?.localPath) && result?.bitmap != null)
            writeToTempImageAndGetPathUri()

        return cursor
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
            result = Result()
            result?.localPath = cursor.getString(getColumn(cursor, projection[0]))
            result?.dateTaken = Date(cursor.getLong(getColumn(cursor, projection[1])))
            result?.bitmap = BitmapFactory.decodeFile(result?.localPath)
        }

        return cursor
    }

    private fun externalStorage(uri: Uri) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val type = split[0]

        if ("primary".equals(type, ignoreCase = true)) {
            result = Result()
            result?.localPath = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            result?.bitmap = BitmapFactory.decodeFile(result?.localPath)
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
            result?.bitmap = BitmapFactory.decodeFile(result?.localPath)
        }

        return cursor
    }

    private fun getData(uri: Uri, id: String?, projection: Array<String>): Cursor? {
        val selection = "_id=?"
        val selectionArgs = arrayOf<String>(id ?: "")

        return context.contentResolver.query(uri, projection, selection, selectionArgs, null)
    }

    private fun getColumn(cursor: Cursor, name: String): Int {
        return cursor.getColumnIndex(name)
    }

    @Throws(IOException::class)
    private fun writeToTempImageAndGetPathUri() {
        val temp = File.createTempFile(
                "_prefix_", /* prefix */
                ".jpg", /* suffix */
                context.cacheDir      /* directory */
        )

        var out: FileOutputStream?
        out = FileOutputStream(temp.absolutePath)
        result?.bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
        result?.localPath = temp.absolutePath
        out.close()
    }
}