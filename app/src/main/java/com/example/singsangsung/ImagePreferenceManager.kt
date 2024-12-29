
package com.example.singsangsung

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

class ImagePreferenceManager(private val context: Context) {
    // 📌 이미지 저장
    fun saveImage(bitmap: Bitmap): String {
        val fileName = "playlist_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
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

//package com.example.singsangsung
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.util.Base64
//import java.io.ByteArrayOutputStream
//
//class ImagePreferenceManager(context:Context) {
//    private val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences("image_prefs", Context.MODE_PRIVATE)
//
//    companion object {
//        private const val IMAGE_KEY = "stored_image"
//    }
//
//    // Bitmap 이미지를 Base64 문자열로 변환하여 저장
//    fun saveImage(bitmap: Bitmap): String {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray = byteArrayOutputStream.toByteArray()
//        val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
//
//        val success = sharedPreferences.edit()
//            .putString("IMAGE_KEY", encodedImage)
//            .commit() // 즉시 저장 확인
//
//        if (success) {
//            return encodedImage
//        } else {
//            throw IllegalStateException("Failed to save image in SharedPreferences")
//        }
//    }
//
//    // Base64 문자열을 Bitmap으로 변환하여 불러오기
//    fun loadImage(): Bitmap? {
//        val encodedImage = sharedPreferences.getString(IMAGE_KEY, null)
//        return if (encodedImage != null) {
//            val byteArray = Base64.decode(encodedImage, Base64.DEFAULT)
//            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        } else {
//            null
//        }
//    }
//
//    // 이미지 데이터 삭제
//    fun clearImage() {
//        sharedPreferences.edit().apply {
//            remove(IMAGE_KEY)
//            apply()
//        }
//    }
//}