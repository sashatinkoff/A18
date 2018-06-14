package com.isidroid.utilsmodule

import android.util.Log

object Diagnostics {
    val TAG = "Diagnostics"
    var DEBUG = BuildConfig.DEBUG

    fun i(msg: String): FileLogger {
        return i(TAG, msg)
    }

    fun i(tag: String, msg: String): FileLogger {
        Log.i(tag, msg)
        return FileLogger(msg)
    }

    fun i(caller: Any, tag: String, message: String): FileLogger {
        var msg: String = "" + caller.javaClass.simpleName + "." + message
        return i(tag, msg)
    }

    fun i(caller: Any, message: String): FileLogger {
        var msg: String = "" + caller.javaClass.simpleName + "." + message
        return i(TAG, msg)
    }

    fun e(msg: String): FileLogger {
        return e(TAG, msg)
    }

    fun e(tag: String, msg: String): FileLogger {
        Log.e(tag, msg)
        return FileLogger(msg)
    }

    fun e(caller: Any, tag: String, message: String): FileLogger {
        var msg: String = "" + caller.javaClass.simpleName + "." + message
        return e(tag, msg)
    }

    fun e(caller: Any, message: String): FileLogger {
        var msg: String = "" + caller.javaClass.simpleName + "." + message
        return e(TAG, msg)
    }

//    private fun getLogFile(filename: String): File {
//        val file = File(Core.application.cacheDir, filename + ".info")
//        return file
//    }

//    fun createLog(filename: String) {
//        val file = getLogFile(filename)
//        if (file.exists()) file.delete()
//        try {
//            file.createNewFile()
//
//            appendLog(filename, "Created at " + Date().toString())
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//    }

    fun appendLog(filename: String, line: String?) {
//        if (TextUtils.isEmpty(line)) return
//
//        val file = getLogFile(filename)
//        if (!file.exists()) createLog(filename)
//
//        try {
//            val bufferedWriter = BufferedWriter(FileWriter(file, true))
//            bufferedWriter.write(line)
//            bufferedWriter.newLine()
//            bufferedWriter.flush()
//            bufferedWriter.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    class FileLogger(val msg: String?) {

        fun append(fileName: String) {
            Diagnostics.appendLog(fileName, msg)
        }
    }
}