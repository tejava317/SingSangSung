package com.example.singsangsung.PlayList

import SongsAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singsangsung.PlaylistAdapter
import com.example.singsangsung.R
import com.example.singsangsung.model.Playlist
import com.example.singsangsung.model.Song
import com.google.gson.Gson
import org.json.JSONArray
import java.io.File

class PlaylistDetailActivityPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var songsAdapter: SongsAdapter
    private val selectedSongs = mutableListOf<Song>() // 선택된 Song 객체 저장
    lateinit var plyName: TextView
    lateinit var plyImage: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab2_playlist_detail)

        // Intent로 전달된 데이터 받기
        val playlistId = intent.getIntExtra("playlistId", -1)
        val playlistName = intent.getStringExtra("playlistName") ?: "제목 없음"
        val playlistImage = intent.getStringExtra("playlistImage") ?: ""

        // UI 요소 초기화
        plyName = findViewById(R.id.tab2_playlist_detail_plyName)
        plyImage = findViewById(R.id.tab2_playlist_detail_image)
        recyclerView = findViewById(R.id.playlistRecyclerView)
        val editPlyName : Button = findViewById(R.id.edit_my_playlist_name)
        val editPlaylist : Button = findViewById(R.id.addSongButton)

        editPlyName.setOnClickListener{
            updatePlaylistInfo()
        }

        editPlaylist.setOnClickListener{
            updatePlaylistSongInfo()
        }

        // UI 업데이트
        plyName.text = playlistName

        if (playlistImage.isNotEmpty()) {
            val imageFile = File(filesDir, playlistImage)
            if (imageFile.exists()) {
                Glide.with(this)
                    .load(imageFile)
                    .into(plyImage)
            } else {
                plyImage.setImageResource(android.R.color.darker_gray)
            }
        }

        // 플레이리스트에 포함된 Song ID로 노래 목록 생성
        val songIds = intent.getIntegerArrayListExtra("playlistSongs") ?: arrayListOf()
        loadSongs(songIds)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        songsAdapter = SongsAdapter(selectedSongs)
        recyclerView.adapter = songsAdapter
    }

private fun updatePlaylistInfo() {
    val parentLayout = plyName.parent as ConstraintLayout
    val index = parentLayout.indexOfChild(plyName)

    // 기존 TextView 제약 조건 저장
    val layoutParams = plyName.layoutParams as ConstraintLayout.LayoutParams

    // EditText 생성 및 설정
    val editText = EditText(this).apply {
        id = R.id.tab2_playlist_detail_plyName // ID 유지
        setText(plyName.text.toString()) // 기존 TextView 값 설정
        textSize = 16f
        setTextColor(resources.getColor(android.R.color.black, null))
        layoutParams.startToStart = layoutParams.startToStart
        layoutParams.endToEnd = layoutParams.endToEnd
        layoutParams.topToTop = layoutParams.topToTop
        layoutParams.bottomToBottom = layoutParams.bottomToBottom
    }

    // TextView 제거 후 EditText 추가
    parentLayout.removeView(plyName)
    parentLayout.addView(editText, index, layoutParams)

    // 버튼 텍스트 변경
    val editPlyName: Button = findViewById(R.id.edit_my_playlist_name)
    editPlyName.text = "저장"

    editPlyName.setOnClickListener {
        val newName = editText.text.toString().trim()
        if (newName.isNotEmpty()) {
            // SharedPreferences 업데이트
            val prefs = getSharedPreferences("playlist_prefs", Context.MODE_PRIVATE)
            val playlists = PlaylistPreferenceManager(this).getPlaylists().toMutableList()
            val playlistId = intent.getIntExtra("playlistId", -1)

            playlists.forEachIndexed { index, playlist ->
                if (playlist.id == playlistId) {
                    playlists[index] = playlist.copy(name = newName)
                }
            }

            prefs.edit().putString("playlist_key", Gson().toJson(playlists)).apply()

            // EditText를 TextView로 교체
            val textView = TextView(this).apply {
                id = R.id.tab2_playlist_detail_plyName
                text = newName
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black, null))
            }

            textView.layoutParams = layoutParams

            parentLayout.removeView(editText)
            parentLayout.addView(textView, index, layoutParams)

            // 버튼 텍스트 원래대로
            editPlyName.text = "편집"
            editPlyName.setOnClickListener {
                updatePlaylistInfo()
            }

            Toast.makeText(this, "플레이리스트 이름이 수정되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}


    private fun updatePlaylistSongInfo() {
        // 다이얼로그 레이아웃 inflate
        val dialogView = layoutInflater.inflate(R.layout.music_checkbox_recyclerview, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.tab2_music_recyclerview)
        val confirmButton: Button = dialogView.findViewById(R.id.button_confirm)

        // 다이얼로그 생성
        val dialog = AlertDialog.Builder(this)
            .setTitle("노래 추가")
            .setView(dialogView)
            .create()

        // 기존 노래 목록 로드
        val allSongs = loadAllSongsFromJson()
        val currentPlaylistSongs = intent.getIntegerArrayListExtra("playlistSongs") ?: arrayListOf()

        // 현재 플레이리스트에 있는 노래는 체크된 상태로 설정
        allSongs.forEach { song ->
            song.isChecked = currentPlaylistSongs.contains(song.id)
        }

        // RecyclerView 설정
        val musicAdapter = PlaylistMusicAdapter(allSongs)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = musicAdapter

        // 저장 버튼 클릭 리스너
        confirmButton.setOnClickListener {
            val selectedIds = musicAdapter.getSelectedIds()

            // 기존 플레이리스트에 선택된 노래 ID 추가
            val updatedPlaylistSongs = currentPlaylistSongs.toMutableSet()
            updatedPlaylistSongs.addAll(selectedIds)

            // SharedPreferences 업데이트
            val prefs = getSharedPreferences("playlist_prefs", Context.MODE_PRIVATE)
            val playlists = PlaylistPreferenceManager(this).getPlaylists().toMutableList()
            val playlistId = intent.getIntExtra("playlistId", -1)

            playlists.forEachIndexed { index, playlist ->
                if (playlist.id == playlistId) {
                    playlists[index] = playlist.copy(checkedMusic = updatedPlaylistSongs.toList())
                }
            }

            prefs.edit().putString("playlist_key", Gson().toJson(playlists)).apply()

            // RecyclerView 새로고침
            loadSongs(updatedPlaylistSongs.toList())
            songsAdapter.notifyDataSetChanged()

            dialog.dismiss()
            Toast.makeText(this, "노래가 추가되었습니다.", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }




    private fun loadSongs(songIds: List<Int>) {
        selectedSongs.clear()
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

                if (id in songIds) {
                    selectedSongs.add(Song(id, title, artist, duration, imageUrl))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun loadAllSongsFromJson(): List<Song> {
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
