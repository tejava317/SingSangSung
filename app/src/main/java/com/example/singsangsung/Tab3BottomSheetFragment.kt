package com.example.singsangsung

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.model.Playlist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray

class Tab3BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 레이아웃 연결
        return inflater.inflate(R.layout.tab_layout3_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
    }

    // RecyclerView 설정
    private fun setupRecyclerView(view: View) {
        val playlists = loadPlaylistsFromAssets() // JSON 데이터 로드
        val recyclerView: RecyclerView = view.findViewById(R.id.tab3PlaylistRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = PlaylistAdapter(playlists) { playlist ->
            // 이미지를 클릭했을 때 빈 화면으로 이동
            navigateToCustomScreen()
        }
    }

    // JSON 파일을 읽어 Playlist 리스트로 변환
    private fun loadPlaylistsFromAssets(): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        try {
            val inputStream = requireContext().assets.open("playlist.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val imageUrl = jsonObject.getString("image_url")
                val name = jsonObject.getString("name")
                val songs = jsonObject.getJSONArray("songs")

                val songList = mutableListOf<Int>()
                for (j in 0 until songs.length()) {
                    songList.add(songs.getInt(j))
                }

                playlists.add(Playlist(imageUrl, name, songList))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return playlists
    }

    // 빈 화면으로 이동
    private fun navigateToCustomScreen() {
        val intent = Intent(requireContext(), Tab3CustomActivity::class.java)
        startActivity(intent)
        dismiss() // Bottom Sheet 닫기
    }
}