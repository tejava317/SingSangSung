package com.example.singsangsung.PlayList

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.singsangsung.R
import java.io.File

class PlaylistDetailActivityPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab2_playlist_detail)

        // Intent로 전달된 데이터 받기
        val playlistId = intent.getIntExtra("playlistId", -1)
        val playlistName = intent.getStringExtra("playlistName") ?: "제목 없음"
        val playlistImage = intent.getStringExtra("playlistImage")

        // UI 요소 초기화
        val plyName: TextView = findViewById(R.id.tab2_playlist_detail_plyName)
        val plyImage: ImageView = findViewById(R.id.tab2_playlist_detail_image)

        plyName.text = playlistName

        if (playlistImage != null) {
            val imageFile = File(filesDir, playlistImage)
            if (imageFile.exists()) {
                Glide.with(this)
                    .load(imageFile)
                    .into(plyImage)
            } else {
                plyImage.setImageResource(android.R.color.darker_gray)
            }
        }
    }
}