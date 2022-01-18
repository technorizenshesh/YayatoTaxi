package com.yayatotaxi.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.util.Log
import java.io.*
import java.lang.Exception
import java.util.*

class Compress {

    private var QTY = 80
    private var mContext: Context? = null
    private var listener: onSuccessListener? = null

    operator fun get(context: Context?): Compress? {
        mContext = context
        return Compress()
    }

    fun setQuality(quality: Int): Compress? {
        QTY = quality
        return this
    }

    fun execute(listener: onSuccessListener?): Compress? {
        this.listener = listener
        return this
    }

    fun CompressedImage(imagePath: String?) {
        if (listener == null) {
            // Toast.makeText(mContext, "Must Call execute", Toast.LENGTH_SHORT).show();
            return
        }
        val maxHeight = 1920.0f
        val maxWidth = 1080.0f
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(imagePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth) as Int
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight) as Int
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(imagePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out = ByteArrayOutputStream()
        scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, QTY, out)
        val byteArray = out.toByteArray()
        Log.e("AfterCompressSize", "===>" + byteArray.size / 1024 + "kb")
        val updatedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        persistImage(updatedBitmap)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    private fun persistImage(bitmap: Bitmap) {
        val calendar = Calendar.getInstance()
        val filesDir = mContext!!.filesDir
        val imageFile = File(filesDir, "CMP" + calendar.timeInMillis + ".jpg")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            if (listener != null) listener!!.response(true, "Success", imageFile)
        } catch (e: Exception) {
            Log.e("Exp", "Error writing bitmap", e)
            listener!!.response(false, "Error writing bitmap", null)
        }
    }

    interface onSuccessListener {
        fun response(status: Boolean, message: String?, file: File?)
    }

}