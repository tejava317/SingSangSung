package com.example.singsangsung

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class Tab3CustomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_layout3_custom)

        // Intent로 전달된 데이터 받기
        val playlistName = intent.getStringExtra("playlist_name") ?: "플레이리스트 제목 없음"
        val playlistImage = intent.getStringExtra("playlist_image")

        // UI 요소 초기화
        val playlistImageView: ImageView = findViewById(R.id.playlistImageView)
        val playlistTitleTextView: TextView = findViewById(R.id.playlistTitleTextView)
        val addDescriptionButton: Button = findViewById(R.id.addDescriptionButton)
        val addSongButton: Button = findViewById(R.id.addSongButton)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)

        // 데이터 바인딩
        playlistTitleTextView.text = playlistName
        playlistImage?.let {
            Glide.with(this)
                .load(it)
                .into(playlistImageView)
        }

        // '설명 추가' 버튼 클릭 리스너
        addDescriptionButton.setOnClickListener {
            showAddDescriptionDialog(addDescriptionButton, descriptionTextView)
        }

        // '노래 추가' 버튼 클릭 리스너
        addSongButton.setOnClickListener {
            // TODO: 노래 추가 로직 구현
        }
    }

    // '설명 추가' 팝업 다이얼로그
    private fun showAddDescriptionDialog(
        addDescriptionButton: Button,
        descriptionTextView: TextView
    ) {
        // 팝업 다이얼로그 생성
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("설명 추가")
            .setMessage("설명을 입력하세요.")
            .setView(editText)
            .setPositiveButton("확인") { _, _ ->
                val inputText = editText.text.toString()
                if (inputText.isNotBlank()) {
                    // 버튼 숨기기
                    addDescriptionButton.visibility = View.GONE
                    // 입력한 텍스트를 TextView에 표시
                    descriptionTextView.text = inputText
                    descriptionTextView.visibility = View.VISIBLE
                }
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }
}