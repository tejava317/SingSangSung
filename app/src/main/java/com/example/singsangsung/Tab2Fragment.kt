package com.example.singsangsung

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.PlayList.GridRecyclerAdapter
import com.example.singsangsung.PlayList.PlaylistDetailFragmentPage
import com.example.singsangsung.PlayList.PlaylistDialogFragment
import com.example.singsangsung.PlayList.PlaylistPreferenceManager


class Tab2Fragment : Fragment() {

    private lateinit var prefs: PlaylistPreferenceManager
    private lateinit var recyclerView: RecyclerView
    private val playlists = mutableListOf<Playlist>()
    private lateinit var gridAdapter : GridRecyclerAdapter
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


    // 📌 RecyclerView 초기화
    private fun setupRecyclerView() {
        gridAdapter = GridRecyclerAdapter(playlists) {
            playlist -> onPlaylistItemClicked(playlist)
        }
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gridAdapter
        }
    }

    // 클릭했을때 디테일 페이지로 이동하는 코드
    private fun onPlaylistItemClicked(playlist: Playlist) {
        val detailFragment = PlaylistDetailFragmentPage().apply {
            arguments = Bundle().apply {
                putInt("playlistId", playlist.id)
                putString("playlistName", playlist.name)
                putString("playlistImage", playlist.imageName)
            }
        }
        try {
            parentFragmentManager.beginTransaction()
                .replace(R.id.tab2_detailPage_container, detailFragment)
                .addToBackStack(null)
                .commit()
        } catch (e: Exception) {
            Log.e("Tab2Fragment Heeju Test detail page", "Fragment Transaction Failed: ${e.message}")
        }
    }

    // 📌 플레이리스트 불러오기
    private fun loadPlaylists() {
        playlists.clear()
        playlists.addAll(prefs.getPlaylists())
        gridAdapter.notifyDataSetChanged()
        Log.d("Tab2Fragment", "Loaded ${playlists.size} playlists.")
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
}
