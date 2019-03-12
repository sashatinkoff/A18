package com.isidroid.pics.utils

import android.net.Uri

object MediaHelper {
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri fileAuthority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri fileAuthority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri fileAuthority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri fileAuthority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return isNewGooglePhotosUri(uri) || isOldGooglePhotosUri(uri)
    }

    fun isGoogleDrive(uri: Uri): Boolean {
        return "com.google.android.apps.docs.storage" == uri.authority
    }

    fun isGoogleDriveLegacy(uri: Uri) = "com.google.android.apps.docs.storage.legacy" == uri.authority

    private fun isNewGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.contentprovider" == uri.authority
    }

    private fun isOldGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}