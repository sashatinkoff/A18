package com.isidroid.a18.pics.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext

/**
 * Created by sashatinkoff on 17.09.16.
 */
object BitmapUtils {
    private// Safe minimum default size
    //        if(true) return IMAGE_MAX_BITMAP_DIMENSION;
    // Get EGL Display
    // Initialise
    // Query total number of configurations
    // Query actual list configurations
    // Iterate through all the configurations to located the maximum texture size
    // Only need to check for width since opengl textures are always squared
    // Keep track of the maximum texture size
    // Release
    // Return largest texture size found, or default
    val maxTextureSize: Int
        get() {
            val IMAGE_MAX_BITMAP_DIMENSION = 1024
            val egl = EGLContext.getEGL() as EGL10
            val display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
            val version = IntArray(2)
            egl.eglInitialize(display, version)
            val totalConfigurations = IntArray(1)
            egl.eglGetConfigs(display, null, 0, totalConfigurations)
            val configurationsList = arrayOfNulls<EGLConfig>(totalConfigurations[0])
            egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations)

            val textureSize = IntArray(1)
            var maximumTextureSize = 0
            for (i in 0 until totalConfigurations[0]) {
                egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize)
                if (maximumTextureSize < textureSize[0])
                    maximumTextureSize = textureSize[0]
            }
            egl.eglTerminate(display)
            return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION)
        }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        val max = Math.max(height, width)

        if (max > reqHeight) {
            var check = max.toFloat() / inSampleSize.toFloat()

            while (check > reqHeight) {
                inSampleSize *= 2
                check = max.toFloat() / inSampleSize.toFloat()
            }
        }

        return inSampleSize
    }

    fun decodeFile(path: String): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(path, options)

        if (options.outWidth == -1 || options.outHeight == -1) return null
        val maxAllowed = maxTextureSize
        options.inSampleSize = calculateInSampleSize(options, maxAllowed, maxAllowed)
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(path, options)
    }

    fun scalePhoto(bitmap: Bitmap, scale: Float): Bitmap {
        var width = (bitmap.width * scale).toInt()
        var height = (bitmap.height * scale).toInt()

        val matrix = Matrix()
        matrix.postScale(scale, scale)
        var result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
        bitmap.recycle()
        return result
    }

    fun scalePhoto(bitmap: Bitmap, size: Int): Bitmap {
        var scale = size.toFloat() / Math.max(bitmap.width, bitmap.height).toFloat()
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        var result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        bitmap.recycle()
        return result
    }
}