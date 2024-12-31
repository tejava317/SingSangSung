package com.example.singsangsung

import SongsAdapter
import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singsangsung.model.Song
import org.json.JSONArray
import java.io.File
import java.io.OutputStream

class Tab3CustomActivity : AppCompatActivity() {

    private lateinit var songsAdapter: SongsAdapter
    private lateinit var convertButton: Button
    private val selectedSongs = mutableListOf<Song>()
    private var descriptionText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_layout3_custom)

        // Intent로 전달된 데이터 받기
        val playlistName = intent.getStringExtra("playlist_name") ?: "플레이리스트 제목 없음"
        val imagefile = intent.getStringExtra("playlist_image")
        // playlist 파일명이 playlistImage임
        val playlistImage = File(filesDir, imagefile)
        val playlistSongs = intent.getIntegerArrayListExtra("playlist_songs") ?: arrayListOf()

        // UI 요소 초기화
        val playlistImageView: ImageView = findViewById(R.id.playlistImageView)
        val playlistTitleTextView: TextView = findViewById(R.id.playlistTitleTextView)
        val addDescriptionButton: Button = findViewById(R.id.addDescriptionButton)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val addSongButton: Button = findViewById(R.id.addSongButton)
        val songRecyclerView: RecyclerView = findViewById(R.id.songRecyclerView)
        convertButton = findViewById(R.id.convertButton) // 변환 버튼 초기화
        convertButton.visibility = View.GONE // 초기 상태 숨김

        // 플레이리스트 데이터 바인딩  : 여기서 이미지가 로드가 안 됨
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

        setupConvertButton()
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
                    descriptionText = inputText // 설명 텍스트 저장

                    // 변환 버튼 표시 조건 체크
                    checkConvertButtonVisibility()
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
                    if (selectedSongs.size >= 3) {
                        Toast.makeText(this, "최대 3개의 노래만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                        selectedItems[which] = false
                    } else {
                        selectedSongs.add(song)
                    }
                } else {
                    selectedSongs.remove(song)
                }
            }
            .setPositiveButton("확인") { _, _ ->
                songsAdapter.notifyDataSetChanged() // RecyclerView 갱신
                addSongButton.visibility = View.GONE

                // 변환 버튼 표시 조건 체크
                checkConvertButtonVisibility()
            }
            .setNegativeButton("취소", null)
            .create()
            .show()
    }

    private fun checkConvertButtonVisibility() {
        if (selectedSongs.isNotEmpty() && descriptionText != null) {
            convertButton.visibility = View.VISIBLE
        }
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

    private fun setupConvertButton() {
        convertButton.setOnClickListener {
            showConvertDialog()
        }
    }

    private fun showConvertDialog() {
        val dialogView = layoutInflater.inflate(R.layout.tab_layout3_convert, null)

        // AlertDialog 빌더 생성
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // 다이얼로그 내부 요소 초기화
        val imageView: ImageView = dialogView.findViewById(R.id.convertedImageView)
        val saveButton: Button = dialogView.findViewById(R.id.saveButton)
        val closeButton: Button = dialogView.findViewById(R.id.closeButton)

        // 이미지 표시 (샘플 이미지 또는 변환된 이미지 설정)
        Glide.with(this)
            .load("https://via.placeholder.com/300.png") // 샘플 이미지 URL
            .placeholder(R.drawable.placeholder)
            .into(imageView)

        // "저장" 버튼 클릭 리스너
        saveButton.setOnClickListener {
            val drawable = imageView.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                saveImageToGallery(bitmap)
                Toast.makeText(this, "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "이미지를 저장할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        // "닫기" 버튼 클릭 리스너
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "converted_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyAppImages")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                outputStream?.close()
            }
        }
    }
}