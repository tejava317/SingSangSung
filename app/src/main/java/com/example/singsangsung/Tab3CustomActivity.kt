package com.example.singsangsung

import SongsAdapter
import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.view.LayoutInflater
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
import com.bumptech.glide.request.RequestOptions
import com.example.singsangsung.model.Song
import jp.wasabeef.glide.transformations.BlurTransformation
import org.json.JSONArray
import java.io.File
import java.io.OutputStream

class Tab3CustomActivity : AppCompatActivity() {

    private lateinit var songsAdapter: SongsAdapter
    private lateinit var convertButton: Button

    private lateinit var playlistName: String
    private lateinit var imagefile: String
    private val selectedSongs = mutableListOf<Song>()
    private var descriptionText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_layout3_custom)

        // Intent로 전달된 데이터 받기
        playlistName = intent.getStringExtra("playlist_name") ?: "플레이리스트 제목 없음"
        imagefile = intent.getStringExtra("playlist_image") ?: "이미지 없음"
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
            exportImage()
        }
    }

    private fun exportImage() {
        val inflater = LayoutInflater.from(this)
        val exportLayout = inflater.inflate(R.layout.export_image, null)

        val background: ImageView = exportLayout.findViewById(R.id.export_background)
        val playlistImageView: ImageView = exportLayout.findViewById(R.id.export_playlist_image)
        val playlistTitleTextView: TextView = exportLayout.findViewById(R.id.export_playlist_title)
        val descriptionTextView: TextView = exportLayout.findViewById(R.id.export_description)

        val playlistImagePath = File(filesDir, imagefile).absolutePath

        // Glide로 플레이리스트 이미지를 로드
        Glide.with(this)
            .load(playlistImagePath)
            .into(playlistImageView)

        // Glide로 배경 이미지에 블러 효과 적용
        Glide.with(this)
            .load(playlistImagePath)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3))) // 블러 강도 설정
            .into(background)

        playlistTitleTextView.text = playlistName
        descriptionTextView.text = descriptionText

        val songImageViews = listOf(
            exportLayout.findViewById<ImageView>(R.id.export_songs_image_1),
            exportLayout.findViewById<ImageView>(R.id.export_songs_image_2),
            exportLayout.findViewById<ImageView>(R.id.export_songs_image_3)
        )
        val songTitleTextViews = listOf(
            exportLayout.findViewById<TextView>(R.id.export_songs_title_1),
            exportLayout.findViewById<TextView>(R.id.export_songs_title_2),
            exportLayout.findViewById<TextView>(R.id.export_songs_title_3)
        )
        val songArtistDurationTextViews = listOf(
            exportLayout.findViewById<TextView>(R.id.export_songs_artist_duration_1),
            exportLayout.findViewById<TextView>(R.id.export_songs_artist_duration_2),
            exportLayout.findViewById<TextView>(R.id.export_songs_artist_duration_3)
        )

        for (i in selectedSongs.indices) {
            val song = selectedSongs[i]
            Glide.with(this).load(song.imageUrl).into(songImageViews[i])
            songTitleTextViews[i].text = song.title
            songArtistDurationTextViews[i].text = "${song.artist} · ${song.duration}"
        }

        val bitmap = createBitmapFromCroppedLayout(exportLayout)

        saveImageToGallery(bitmap)

        // 5. 다이얼로그에 저장된 이미지 불러오기
        Thread.sleep(1000)
        showConvertDialog(bitmap)
    }

    private fun createBitmapFromCroppedLayout(view: View): Bitmap {
        // 전체 레이아웃 크기 측정 및 비트맵 생성
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val fullBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(fullBitmap)
        view.draw(canvas)

        // 크롭할 영역의 크기 설정
        val cropWidth = dpToPx(324) // 324dp -> 픽셀 변환
        val cropHeight = dpToPx(576) // 576dp -> 픽셀 변환
        val cropStartX = 0 // 왼쪽 상단 X 좌표
        val cropStartY = 0 // 왼쪽 상단 Y 좌표

        // 크롭된 비트맵 생성
        return Bitmap.createBitmap(fullBitmap, cropStartX, cropStartY, cropWidth, cropHeight)
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "converted_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/SingSangSung")
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

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun showConvertDialog(bitmap: Bitmap) {
        val dialogView = layoutInflater.inflate(R.layout.tab_layout3_convert, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val imageView: ImageView = dialogView.findViewById(R.id.convertedImageView)
        val closeButton: Button = dialogView.findViewById(R.id.closeButton)

        imageView.setImageBitmap(bitmap)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}