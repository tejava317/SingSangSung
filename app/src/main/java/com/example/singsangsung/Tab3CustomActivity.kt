package com.example.singsangsung

import SongsAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singsangsung.model.Song
import org.json.JSONArray

class Tab3CustomActivity : AppCompatActivity() {

    private lateinit var songsAdapter: SongsAdapter
    private val selectedSongs = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_layout3_custom)

        // Intent로 전달된 데이터 받기
        val playlistName = intent.getStringExtra("playlist_name") ?: "플레이리스트 제목 없음"
        val playlistImage = intent.getStringExtra("playlist_image")
        val playlistSongs = intent.getIntegerArrayListExtra("playlist_songs") ?: arrayListOf()

        // UI 요소 초기화
        val playlistImageView: ImageView = findViewById(R.id.playlistImageView)
        val playlistTitleTextView: TextView = findViewById(R.id.playlistTitleTextView)
        val addDescriptionButton: Button = findViewById(R.id.addDescriptionButton)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val addSongButton: Button = findViewById(R.id.addSongButton)
        val songRecyclerView: RecyclerView = findViewById(R.id.songRecyclerView)

        // 플레이리스트 데이터 바인딩
        playlistTitleTextView.text = playlistName
        Glide.with(this).load(playlistImage).into(playlistImageView)

        // RecyclerView 초기화
        songsAdapter = SongsAdapter(selectedSongs)
        songRecyclerView.layoutManager = LinearLayoutManager(this)
        songRecyclerView.adapter = songsAdapter

        // "설명 추가" 버튼 클릭 리스너
        addDescriptionButton.setOnClickListener {
            showAddDescriptionDialog(addDescriptionButton, descriptionTextView)
        }

        // '노래 추가' 버튼 클릭 리스너
        addSongButton.setOnClickListener {
            showSongSelectionDialog(addSongButton, playlistSongs)
        }
    }

    private fun showAddDescriptionDialog(
        addDescriptionButton: Button,
        descriptionTextView: TextView
    ) {
        val editText = EditText(this) // EditText 생성
        val dialog = AlertDialog.Builder(this)
            .setTitle("설명 추가")
            .setMessage("설명을 입력하세요.")
            .setView(editText) // 다이얼로그에 EditText 추가
            .setPositiveButton("확인") { _, _ ->
                val inputText = editText.text.toString()
                if (inputText.isNotBlank()) {
                    addDescriptionButton.visibility = View.GONE // 버튼 숨기기
                    descriptionTextView.text = inputText // 입력한 텍스트 설정
                    descriptionTextView.visibility = View.VISIBLE // TextView 표시
                }
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show() // 다이얼로그 표시
    }

    private fun showSongSelectionDialog(addSongButton: Button, playlistSongs: List<Int>) {
        val allSongs = loadSongsFromAssets() // 모든 노래 로드
        val playlistSongsData = allSongs.filter { playlistSongs.contains(it.id) } // 필터링된 노래
        val songNames = playlistSongsData.map { it.title }.toTypedArray()
        val selectedItems = BooleanArray(songNames.size)

        AlertDialog.Builder(this)
            .setTitle("노래 추가")
            .setMultiChoiceItems(songNames, selectedItems) { _, which, isChecked ->
                val song = playlistSongsData[which]
                if (isChecked) {
                    selectedSongs.add(song)
                } else {
                    selectedSongs.remove(song)
                }
            }
            .setPositiveButton("확인") { _, _ ->
                songsAdapter.notifyDataSetChanged() // RecyclerView 갱신
                addSongButton.visibility = View.GONE
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

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