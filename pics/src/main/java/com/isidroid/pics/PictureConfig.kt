package com.isidroid.pics

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent

class PictureConfig private constructor() {
    internal lateinit var authority: String
    internal lateinit var context: Context
    var codeTakePicture: Int = 3000
    var codePickPicture: Int = 3001


    fun withContext(context: Context) = apply { this.context = context.applicationContext }
    fun withPackage(pkg: String) = apply { this.authority = "$pkg.fileprovider" }
    fun withAuthority(auth: String) = apply { this.authority = auth }
    fun withCodeTakePicture(requestCode: Int) = apply { this.codeTakePicture = requestCode }
    fun withCodePickPicture(requestCode: Int) = apply { this.codePickPicture = requestCode }

    fun isSuccess(requestCode: Int, resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK && requestCode in codeTakePicture..codePickPicture
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var self = PictureConfig()

        fun get() = self
    }
}