//package com.example.singsangsung;
//
//import SongsAdapter
//import android.health.connect.datatypes.units.Length
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.singsangsung.PlayList.SongPreferenceManager
//import com.example.singsangsung.PlayList.Tab1AddplaylistDialogFragment
//import com.example.singsangsung.model.Song
//import org.json.JSONArray
//import org.json.JSONObject
//
//class Tab1Fragment : Fragment() {
//
//    lateinit var addplyBtn : Button
//    lateinit var songManager : SongPreferenceManager
//    lateinit var recyclerView: RecyclerView
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.tab_layout1, container, false)
//
//        recyclerView= view.findViewById(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        songManager = SongPreferenceManager(requireContext())
//
//        val songs = loadSongsFromAssets()
//        //recyclerView.adapter = SongsAdapter(songs)
//        recyclerView.adapter = SongsAdapter(songManager.getSongs().toMutableList())
//
//        addplyBtn = view.findViewById(R.id.tab1_addPlaylist)
//
//        addplyBtn.setOnClickListener{
//            addOneSonglist()
//        }
//
//
//        return view
//    }
//
//    private fun addOneSonglist(){
//        openAddSongDialog()
//        Toast.makeText(requireContext(),"버튼 눌림",Toast.LENGTH_SHORT).show()
//
//    }
//
//    private fun openAddSongDialog(){
//        val open = Tab1AddplaylistDialogFragment()
//        open.setOnDismissListener{
//           refreshSongList()
//        }
//        open.show(parentFragmentManager, "Add Song Dialog")
//    }
//
//    private fun refreshSongList() {
//        val updatedSongs = songManager.getSongs()
//        (recyclerView.adapter as? SongsAdapter)?.apply {
//            songs.clear()
//            songs.addAll(updatedSongs)
//            notifyDataSetChanged()
//        }
//    }
//
//    private fun loadSongsFromAssets(): List<Song> {
//        val songs = mutableListOf<Song>()
//        try {
//            val inputStream = requireContext().assets.open("songs.json")
//            val jsonString = inputStream.bufferedReader().use { it.readText() }
//            val jsonArray = JSONArray(jsonString)
//
//            for (i in 0 until jsonArray.length()) {
//                val jsonObject = jsonArray.getJSONObject(i)
//                val id = jsonObject.getInt("id")
//
//                val title = jsonObject.getString("title")
//                val artist = jsonObject.getString("artist")
//                val duration = jsonObject.getString("duration")
//                val imageUrl = jsonObject.getString("image_url")
//                songs.add(Song(id, title, artist, duration, imageUrl))
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return songs
//    }
//}

package com.example.singsangsung

import SongsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.PlayList.SongPreferenceManager
import com.example.singsangsung.PlayList.Tab1AddplaylistDialogFragment

class Tab1Fragment : Fragment() {

    private lateinit var addplyBtn: Button
    private lateinit var songManager: SongPreferenceManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SongsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tab_layout1, container, false)

        // 초기화
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        songManager = SongPreferenceManager(requireContext())
        adapter = SongsAdapter(songManager.getSongs())
        recyclerView.adapter = adapter

        initializeSongsFromJson()
        addplyBtn = view.findViewById(R.id.tab1_addPlaylist)
        addplyBtn.setOnClickListener {
            addOneSonglist()
        }

        return view
    }

    private fun initializeSongsFromJson() {
        val isFirstRun = songManager.getSongs().isEmpty()
        if (isFirstRun) {
            songManager.initializeFromJson(requireContext())
            refreshSongList()
            Toast.makeText(requireContext(), "JSON 데이터를 SharedPreferences로 이전했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "이미 데이터가 이전되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 📌 노래 추가 Dialog 호출
    private fun addOneSonglist() {
        openAddSongDialog()
        Toast.makeText(requireContext(), "버튼 눌림", Toast.LENGTH_SHORT).show()
    }

    // 📌 DialogFragment 열기
    private fun openAddSongDialog() {
        val open = Tab1AddplaylistDialogFragment()
//        open.setOnDismissListener {
//            refreshSongList()
//        }
        open.show(parentFragmentManager, "AddSongDialog")
    }

    // 📌 RecyclerView 새로고침
    private fun refreshSongList() {
        val updatedSongs = songManager.getSongs()
        adapter.songs = updatedSongs // 새로운 리스트로 교체
        adapter.notifyDataSetChanged()
    }
}
