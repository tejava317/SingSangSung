
package com.example.singsangsung.PlayList

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
        val croppedBitmap = cropToSquare(bitmap)

        // 파일로 저장
        FileOutputStream(file).use { fos ->
            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }
//        FileOutputStream(file).use { fos ->
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//        }
        return fileName
    }

    // 📌 이미지 파일 경로
    fun getImageFile(fileName: String): File {
        return File(context.filesDir, fileName)
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        Log.d("Heeju Debug", "Original Size: Width = $width, Height = $height")
        // 가장 짧은 변을 기준으로 1:1 영역 설정
        val size = minOf(width, height)

        // 이미지 중앙에서 크롭 시작점 계산
        val xOffset = (width - size) / 2
        val yOffset = (height - size) / 2
        val croppedBitmap = Bitmap.createBitmap(bitmap, xOffset, yOffset, size, size)
        Log.d("Heeju Debug", "Cropped Size: Width = ${croppedBitmap.width}, Height = ${croppedBitmap.height}")
        return croppedBitmap
    }
}

