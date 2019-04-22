package com.isidroid.a18

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.sample_main.*
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.util.*


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener()).check()

        val jsonobject = JSONObject().put("name", "Hello world")
            .put("data", UUID.randomUUID().toString())

        btnSave.setOnClickListener { imageview.setImageBitmap(createQR(jsonobject.toString())) }
        btnPdf.setOnClickListener { readQR() }
    }

    private fun qrbitmap(): Bitmap {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "qr_json.png")
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    private fun readQR() {
        val bitmap = qrbitmap()
        val intArray = IntArray(bitmap.width * bitmap.height)

        //copy pixel data from the Bitmap into the 'intArray' array
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        val reader = MultiFormatReader()
        val result = reader.decode(binaryBitmap)
        val data = result.text

        Timber.i("data=$data")
    }
    private fun createQR(text: String): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }
            bmp

        } catch (e: WriterException) {
            null
        }
    }
}