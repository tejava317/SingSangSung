
package com.example.singsangsung

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class ImagePreferenceManager(private val context: Context) {
    // 📌 이미지 저장
    fun saveImage(bitmap: Bitmap): String {
        val fileName = "playlist_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        Log.d("Heeju file Test", file.absolutePath)
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }
        return fileName
    }

    // 📌 이미지 파일 경로
    fun getImageFile(fileName: String): File {
        return File(context.filesDir, fileName)
    }
}

