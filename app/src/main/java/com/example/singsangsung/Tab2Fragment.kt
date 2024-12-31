package com.example.singsangsung

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.model.Playlist
import com.example.singsangsung.PlayList.GridRecyclerAdapter
import com.example.singsangsung.PlayList.PlaylistDetailActivityPage
import com.example.singsangsung.PlayList.PlaylistPreferenceManager
import com.example.singsangsung.model.PlayList.PlaylistDialogFragment


class Tab2Fragment : Fragment() {

    private lateinit var prefs: PlaylistPreferenceManager
    private lateinit var recyclerView: RecyclerView
    private val playlists = mutableListOf<Playlist>()
    private lateinit var gridAdapter: GridRecyclerAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Tab2Fragment", "onAttach 호출됨")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Tab2Fragment", "onCreateView 호출됨")
        return inflater.inflate(R.layout.tab_layout2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("heeju", "recycler view start--")
        super.onViewCreated(view, savedInstanceState)

        prefs = PlaylistPreferenceManager(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)

        setupRecyclerView()
        loadPlaylists()

        val addButton: Button = view.findViewById(R.id.add_playlist)
        addButton.setOnClickListener {
            openPlaylistDialog()
        }
    }

    private fun setupRecyclerView() {
        gridAdapter = GridRecyclerAdapter(playlists,
            onItemClick = { playlist -> onPlaylistItemClicked(playlist) },
            onItemLongClick = { playlist -> onPlaylistItemLongClicked(playlist) } // 길게 누르기 이벤트 추가
        )
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gridAdapter
        }
    }

    // 📌 플레이리스트 불러오기
    private fun loadPlaylists() {
        playlists.clear()
        playlists.addAll(prefs.getPlaylists())
        gridAdapter.notifyDataSetChanged()
        Log.d("Tab2Fragment", "Loaded ${playlists.size} playlists.")
    }

    private fun onPlaylistItemLongClicked(playlist: Playlist) {
        AlertDialog.Builder(requireContext())
            .setTitle("플레이리스트 삭제")
            .setMessage("정말로 '${playlist.name}'을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                deletePlaylist(playlist)
            }
            .setNegativeButton("취소", null)
            .show()
    }
    private fun deletePlaylist(playlist: Playlist) {
        val playlists = prefs.getPlaylists().toMutableList()
        playlists.removeAll { it.id == playlist.id } // 해당 ID를 가진 Playlist 삭제

        // 📌 ID 재정렬
        val reorderedPlaylists = playlists.mapIndexed { index, p ->
            p.copy(id = index + 1)
        }

        // 📌 SharedPreferences에 저장
        prefs.savePlaylists(reorderedPlaylists)
        prefs.updateLastId(reorderedPlaylists.size) // LAST_ID_KEY 업데이트

        // 📌 RecyclerView 새로고침
        loadPlaylists()
        Toast.makeText(requireContext(), "'${playlist.name}'이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }
    private fun openPlaylistDialog() {
        val dialog = PlaylistDialogFragment()
        dialog.setOnDismissListener(object : PlaylistDialogFragment.OnDismissListener {
            override fun onDismiss() {
                loadPlaylists()
            }
        })
        dialog.show(parentFragmentManager, "PlaylistDialog")
    }

    override fun onResume() {
        super.onResume()
        loadPlaylists()
    }

    private fun onPlaylistItemClicked(playlist: Playlist) {
        val intent = Intent(requireContext(), PlaylistDetailActivityPage::class.java).apply {
            putExtra("playlistId", playlist.id)
            putExtra("playlistName", playlist.name)
            putExtra("playlistImage", playlist.imageName)
            putIntegerArrayListExtra("playlistSongs", ArrayList(playlist.checkedMusic)) // Song ID 목록 전달
        }
        startActivity(intent)
    }
}

