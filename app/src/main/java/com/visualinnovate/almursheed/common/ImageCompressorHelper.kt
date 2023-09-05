package com.visualinnovate.almursheed.common

import android.content.Context
import android.graphics.*
import com.visualinnovate.almursheed.R
import java.io.*
import java.lang.Exception

class ImageCompressorHelper(private val context: Context) {
    private val maxHeight = 1280.0f
    private val maxWidth = 1280.0f
    private var imagePath: String? = null

    fun setImagePath(imagePath: String): ImageCompressorHelper {
        this.imagePath = imagePath
        return this
    }

    fun getImagePath(): String? {
        return imagePath
    }

    // Function to compress an image
    fun compressImage(error: (String) -> Unit = {}, absoluteImagePath: (String) -> Unit = {}) {
        try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val scaledBitmap = scaleBitmap(bitmap, maxWidth, maxHeight)

            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream) // Adjust quality (0-100)
            val tempPath = writeToCacheFile(outputStream)
            if (tempPath.isNotEmpty()) {
                absoluteImagePath.invoke(tempPath)
            } else {
                error.invoke(context.resources.getString(R.string.enter_your_id))
            }
        } catch (ex: Exception) {
            error.invoke(context.resources.getString(R.string.enter_your_id))
        }
    }

    private fun writeToCacheFile(outputStream: ByteArrayOutputStream): String {
        val out: FileOutputStream?
        var returnPath = ""
        try {
            val f = File(context.applicationContext.cacheDir, "image_${System.currentTimeMillis()}.png")
            out = FileOutputStream(f)
            val data = outputStream.toByteArray()
            out.run {
                write(data)
                flush()
                close()
            }
            returnPath = f.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return returnPath
    }

    // Function to scale a bitmap while maintaining aspect ratio
    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Float, maxHeight: Float): Bitmap {
        val scaleFactor = (maxWidth / bitmap.width).coerceAtMost(maxHeight / bitmap.height)
        val scaledWidth = (bitmap.width * scaleFactor).toInt()
        val scaledHeight = (bitmap.height * scaleFactor).toInt()

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

    companion object {
        fun with(context: Context): ImageCompressorHelper {
            return ImageCompressorHelper(context)
        }
    }
}
