package com.example.singsangsung

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.PlayList.PlaylistPreferenceManager
import com.example.singsangsung.model.Playlist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONArray

class Tab3BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var manager : PlaylistPreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 레이아웃 연결
        return inflater.inflate(R.layout.tab_layout3_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = PlaylistPreferenceManager(requireContext())
        setupRecyclerView(view)
    }

    // RecyclerView 설정
    private fun setupRecyclerView(view: View) {
        val playlists = loadPlaylistsFromAssets() // JSON 데이터 로드
        val recyclerView: RecyclerView = view.findViewById(R.id.tab3PlaylistRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        recyclerView.adapter = PlaylistAdapter(playlists) { playlist ->
            navigateToCustomScreen(playlist) // 클릭된 Playlist 객체 전달
        }
    }

    // JSON 파일을 읽어 Playlist 리스트로 변환
    private fun loadPlaylistsFromAssets(): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        try {
            val playlistList = manager.getPlaylists()
            for (playlist in playlistList) {
                val id = playlist.id
                val imageName = playlist.imageName
                val title = playlist.name
                val songs = playlist.checkedMusic
//                val songList = mutableListOf<Int>()
//                for (song in songs) {
//                    songList.add(song)
//                }

                playlists.add(Playlist(id, title, imageName, songs))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return playlists
    }

    // 빈 화면으로 이동
    private fun navigateToCustomScreen(playlist: Playlist) {
        val intent = Intent(requireContext(), Tab3CustomActivity::class.java).apply {
            putExtra("playlist_name", playlist.name)

            putExtra("playlist_image", playlist.imageName)
            putIntegerArrayListExtra("playlist_songs", ArrayList(playlist.checkedMusic))
        }
        startActivity(intent)
        dismiss() // Bottom Sheet 닫기
    }
}