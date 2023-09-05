package com.visualinnovate.almursheed.common.permission

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.visualinnovate.almursheed.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileUtils constructor(private val context: Context) {

    private var filePath = ""
    private var fileName = ""

    fun getFilePath(uri: Uri?, error: (String) -> Unit = {}, imagePath: (String) -> Unit = {}) {
        try {
            if (uri != null) {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor =
                    context.applicationContext.contentResolver.query(
                        uri,
                        filePathColumn,
                        null,
                        null,
                        null
                    )
                if (cursor != null) {
                    try {
                        val columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0])
                        cursor.moveToFirst()
                        createTmpFileUri()
                        imagePath.invoke(cursor.getString(columnIndex))
                    } catch (ex: Exception) {
                        error.invoke("error")
                    }
                    cursor.close()
                } else {
                    error.invoke("error")
                }
            }
        } catch (ex: Exception) {
            error.invoke("error")
        }
    }

    private fun generateTimestampedFilename(originalFilename: String): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val extension = originalFilename.substringAfterLast('.', "")

        return "${timeStamp}_${originalFilename.replace(".", "_")}.$extension"
    }

    fun createTmpFileUri(): Uri { // create temp file to save image from camera
        val tmpFile =
            File.createTempFile(
                generateTimestampedFilename("tmp_image_file"),
                ".png",
                context.applicationContext.cacheDir
            )
                .apply {
                    createNewFile()
                    //  deleteOnExit()
                }
        filePath = tmpFile.absolutePath
        fileName = tmpFile.name
        return FileProvider.getUriForFile(
            context.applicationContext,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            tmpFile
        )
    }

    fun clearTempFile() {
        File(context.applicationContext.cacheDir, fileName).delete()
    }

    fun checkTmpFileLength(): Boolean {
        return if (fileName.isNotEmpty()) {
            try {
                val cacheDir = context.applicationContext.cacheDir
                val file = File(cacheDir, fileName)
                file.length() > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            false
        }
    }

    fun getRealPathFromURI(): String {
        return filePath
    }
}
