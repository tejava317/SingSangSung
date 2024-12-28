package com.example.singsangsung

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.singsangsung.model.Song
import org.json.JSONArray

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
            showSongSelectionDialog(
                playlistSongs = intent.getIntegerArrayListExtra("playlist_songs") ?: arrayListOf(),
                addSongButton = addSongButton,
                songContainer = findViewById(R.id.songContainer)
            )
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

    // '노래 추가' 팝업 다이얼로그
    private fun showSongSelectionDialog(
        playlistSongs: List<Int>,
        addSongButton: Button,
        songContainer: LinearLayout
    ) {
        val songs = loadSongsFromAssets() // 모든 노래 정보
        val playlistSongsData = songs.filter { playlistSongs.contains(it.id) } // 플레이리스트 노래 필터링
        val selectedSongs = mutableListOf<Song>()

        val songNames = playlistSongsData.map { it.title }.toTypedArray() // 노래 제목 리스트
        val selectedItems = BooleanArray(songNames.size) // 선택 상태 저장

        val dialog = AlertDialog.Builder(this)
            .setTitle("노래 추가")
            .setMultiChoiceItems(songNames, selectedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedSongs.add(playlistSongsData[which])
                } else {
                    selectedSongs.remove(playlistSongsData[which])
                }
            }
            .setPositiveButton("확인") { _, _ ->
                if (selectedSongs.isNotEmpty()) {
                    // '노래 추가' 버튼 숨기기
                    addSongButton.visibility = View.GONE

                    // 선택한 노래를 표시
                    selectedSongs.forEach { song ->
                        val textView = TextView(this).apply {
                            text = "${song.title} - ${song.artist}"
                            textSize = 16f
                            setPadding(8, 8, 8, 8)
                        }
                        songContainer.addView(textView)
                    }
                }
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }

    // JSON 파일에서 모든 노래 데이터를 로드
    private fun loadSongsFromAssets(): List<Song> {
        val songs = mutableListOf<Song>()
        try {
            val inputStream = assets.open("songs.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val title = jsonObject.getString("title")
                val artist = jsonObject.getString("artist")
                val duration = jsonObject.getString("duration")
                val imageUrl = jsonObject.getString("image_url")
                songs.add(Song(id, title, artist, duration, imageUrl))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return songs
    }
}