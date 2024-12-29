package com.example.singsangsung

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tab2Fragment : Fragment() {

    private lateinit var prefs: PlaylistPreferenceManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
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
        //updateEmptyMessageVisibility()

        val addButton: Button = view.findViewById(R.id.add_playlist)
        addButton.setOnClickListener {
            openPlaylistDialog()
        }
    }


    /**
     * RecyclerView 설정
     */
    private fun setupRecyclerView() {
        Log.d("heeju", "recycler view start")
        playlists.addAll(prefs.getPlaylists())
        gridAdapter = GridRecyclerAdapter(playlists)
        Log.d("heeju", "recycler view start2")
        Log.d("Tab2Fragment", "Playlists loaded: ${playlists.size}")

        Log.d("Tab2Fragment", "Playlists loaded: ${playlists.size}") // 로그로 확인

        if (playlists.isEmpty()) {
            Log.e("Tab2Fragment", "Playlists is empty after loading from prefs!")
        }

        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2열 GridLayout
            adapter = gridAdapter
        }
    }



//    private fun setupRecyclerView() {
//        playlists.clear()
//        playlists.addAll(prefs.getPlaylists())
//
//        playlistAdapter = PlaylistAdapter(playlists).apply {
//            setOnItemClickListener { playlist ->
//                Toast.makeText(
//                    requireContext(),
//                    "플레이리스트 선택: ${playlist.name}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Log.d("Playlist Click", "ID: ${playlist.id}, Name: ${playlist.name}")
//            }
//
//            setOnItemLongClickListener { playlist ->
//                removePlaylist(playlist)
//            }
//        }
//
//        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//        recyclerView.adapter = playlistAdapter
//    }

    /**
     * 빈 목록 메시지 표시 여부 업데이트
     */
//    private fun updateEmptyMessageVisibility() {
//        if (playlists.isEmpty()) {
//            emptyMessage.visibility = View.VISIBLE
//        } else {
//            emptyMessage.visibility = View.GONE
//        }
//    }

    /**
     * 플레이리스트 다이얼로그 열기
     */
    private fun openPlaylistDialog() {
        val dialog = PlaylistDialogFragment()
        dialog.setOnDismissListener(object : PlaylistDialogFragment.OnDismissListener {
            override fun onDismiss() {
                refreshRecyclerView()
            }
        })
        dialog.show(parentFragmentManager, "PlaylistDialog")
    }

    /**
     * 플레이리스트 삭제
     */
//    private fun removePlaylist(playlist: Playlist) {
//        prefs.removePlaylist(playlist.id)
//        playlistAdapter.removeItem(playlist)
//        updateEmptyMessageVisibility()
//        Toast.makeText(requireContext(), "플레이리스트 삭제됨: ${playlist.name}", Toast.LENGTH_SHORT).show()
//    }

    /**
     * RecyclerView 새로고침
     */
    private fun refreshRecyclerView() {
        playlists.clear()
        playlists.addAll(prefs.getPlaylists())
        gridAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }
}

/*package com.example.singsangsung

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tab2Fragment : Fragment() {
    private lateinit var prefs: PlaylistPreferenceManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var emptyMessage: TextView // 빈 메시지 표시
    private val playlists = mutableListOf<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.tab_layout2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = PlaylistPreferenceManager(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)


        setupRecyclerView()
        updateEmptyMessageVisibility()

        val addButton: Button = view.findViewById(R.id.add_playlist)
        addButton.setOnClickListener {
            val dialog = PlaylistDialogFragment()
            dialog.setOnPlaylistAddedListener { playlist ->
                addPlaylistToRecyclerView(playlist)
            }
            dialog.show(parentFragmentManager, "PlaylistDialog")
        }
    }

    private fun setupRecyclerView() {
        playlists.clear()
        playlists.addAll(prefs.getPlaylists())

        playlistAdapter = PlaylistAdapter(playlists).apply {
            setOnItemClickListener { playlist ->
                Toast.makeText(
                    requireContext(),
                    "플레이리스트 선택: ${playlist.name}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("Playlist Click", "ID: ${playlist.id}, Name: ${playlist.name}")
            }

            setOnItemLongClickListener { playlist ->
                removePlaylist(playlist)
            }
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistAdapter
    }

    private fun addPlaylistToRecyclerView(playlist: Playlist) {
        playlists.add(playlist)
        playlistAdapter.notifyItemInserted(playlists.size - 1)
        updateEmptyMessageVisibility()
    }

    private fun removePlaylist(playlist: Playlist) {
        prefs.removePlaylist(playlist.id)
        playlistAdapter.removeItem(playlist)
        updateEmptyMessageVisibility()
        Toast.makeText(requireContext(), "플레이리스트 삭제됨: ${playlist.name}", Toast.LENGTH_SHORT).show()
    }

    private fun updateEmptyMessageVisibility() {
        if (playlists.isEmpty()) {
            emptyMessage.visibility = View.VISIBLE
        } else {
            emptyMessage.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        playlists.clear()
        playlists.addAll(prefs.getPlaylists())
        playlistAdapter.notifyDataSetChanged()
        updateEmptyMessageVisibility()
    }
} */

//package com.example.singsangsung;
//
//import android.os.Bundle;
//import android.util.Log
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//
//class Tab2Fragment : Fragment() {
//    private lateinit var prefs: PlaylistPreferenceManager
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var playlistAdapter: PlaylistAdapter
//    private lateinit var emptyMessage: TextView // 플레이리스트가 비어있을 때 표시할 메시지
//    private val playlists = mutableListOf<Playlist>()
//
//    override fun onCreateView(inflater : LayoutInflater , container : ViewGroup? , savedInstanceState: Bundle?) : View {
//        return inflater.inflate(R.layout.tab_layout2, container, false);
//    }
//
//    // 왜 R만 써도 인식되는건지 확인
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        prefs = PlaylistPreferenceManager(requireContext())
//        recyclerView = view.findViewById(R.id.recyclerView)
//
//        setupRecyclerView()
//        updateEmptyMessageVisibility()
//
//        val addButton: Button = view.findViewById(R.id.add_playlist)
//        addButton.setOnClickListener {
//            val dialog = PlaylistDialogFragment()
//            dialog.show(parentFragmentManager, "PlaylistDialog")
//        }
//
//         // 최종 플레이리스트 저장
//        val addPlaylist : Button = view.findViewById(R.id.button_add)
//        addPlaylist.setOnClickListener{
//            val playlists = prefs.getPlaylists()
//            playlists.forEach { playlist ->
//                Log.e("heeju", "${playlist.name} and ${playlist.imageUrl} and ${playlist.id}")
//            }
//        }
//
//    }
//
//    private fun setupRecyclerView() {
//        playlists.clear()
//        playlists.addAll(prefs.getPlaylists())
//
//        playlistAdapter = PlaylistAdapter(playlists).apply {
//
//            setOnItemClickListener { playlist ->
//                Toast.makeText(
//                    requireContext(),
//                    "플레이리스트 선택: ${playlist.name}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Log.d("Playlist Click", "ID: ${playlist.id}, Name: ${playlist.name}")
//            }
//
//            setOnItemLongClickListener { playlist ->
//                removePlaylist(playlist)
//            }
//        }
//
//        recyclerView.apply {
//            layoutManager = GridLayoutManager(requireContext(), 2) // 2열의 GridLayout
//            adapter = playlistAdapter
//        }
//    }
//
//    /**
//     * RecyclerView 아이템 삭제
//     */
//    private fun removePlaylist(playlist: Playlist) {
//        prefs.removePlaylist(playlist.id) // ID로 플레이리스트 삭제
//        playlists.remove(playlist)
//        playlistAdapter.notifyDataSetChanged()
//        updateEmptyMessageVisibility()
//        Toast.makeText(requireContext(), "플레이리스트 삭제됨: ${playlist.name}", Toast.LENGTH_SHORT).show()
//    }
//
//    /**
//     * 빈 메시지 표시 여부 업데이트
//     */
//    private fun updateEmptyMessageVisibility() {
//        if (playlists.isEmpty()) {
//            emptyMessage.visibility = View.VISIBLE
//        } else {
//            emptyMessage.visibility = View.GONE
//        }
//    }
//
//    /**
//     * RecyclerView 새로고침
//     */
//    private fun refreshRecyclerView() {
//        playlists.clear()
//        playlists.addAll(prefs.getPlaylists())
//        playlistAdapter.notifyDataSetChanged()
//        updateEmptyMessageVisibility()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        refreshRecyclerView()
//    }
//
//
//}

//package com.example.singsangsung
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//
//class Tab2Fragment : Fragment() {
//    private lateinit var prefs: PlaylistPreferenceManager
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var playlistAdapter: PlaylistAdapter
//    private val playlists = mutableListOf<Playlist>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.tab_layout2, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        prefs = PlaylistPreferenceManager(requireContext())
//        recyclerView = view.findViewById(R.id.recyclerView)
//
//        setupRecyclerView()
//
//        val addButton: Button = view.findViewById(R.id.add_playlist)
//        addButton.setOnClickListener {
//            val dialog = PlaylistDialogFragment()
//            dialog.setOnPlaylistAddedListener(object : PlaylistDialogFragment.OnPlaylistAddedListener {
//                override fun onPlaylistAdded(playlist: Playlist) {
//                    addPlaylistToRecyclerView(playlist)
//                }
//            })
//            dialog.show(parentFragmentManager, "PlaylistDialog")
//        }
//    }
//
//    private fun setupRecyclerView() {
//        playlists.clear()
//        playlists.addAll(prefs.getPlaylists())
//
//        playlistAdapter = PlaylistAdapter(playlists)
//        recyclerView.apply {
//            layoutManager = GridLayoutManager(requireContext(), 2) // 2개씩 배치
//            adapter = playlistAdapter
//        }
//    }
//
//    private fun addPlaylistToRecyclerView(playlist: Playlist) {
//        playlists.add(playlist)
//        playlistAdapter.notifyItemInserted(playlists.size - 1)
//    }
//}
