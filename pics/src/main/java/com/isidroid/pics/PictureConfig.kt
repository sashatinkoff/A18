package com.isidroid.pics

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

class PictureConfig private constructor() {
    internal lateinit var authority: String
    var codeTakePicture: Int = 3000
    var codePick: Int = 3001


    fun withPackage(pkg: String) = apply { this.authority = "$pkg.fileprovider" }
    fun withAuthority(auth: String) = apply { this.authority = auth }
    fun withCodeTakePicture(requestCode: Int) = apply { this.codeTakePicture = requestCode }
    fun withCodePickPicture(requestCode: Int) = apply { this.codePick = requestCode }

    fun isSuccess(requestCode: Int, resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK && requestCode in codeTakePicture..codePick
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var self = PictureConfig()

        fun get() = self
    }
}