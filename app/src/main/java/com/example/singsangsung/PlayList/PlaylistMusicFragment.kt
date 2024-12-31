package com.example.singsangsung.PlayList

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.R
import com.example.singsangsung.model.Song
import org.json.JSONArray
class PlaylistMusicFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistMusicAdapter
    private lateinit var confirmButton: Button

    var onSongsSelected: ((List<Int>) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.music_checkbox_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.tab2_music_recyclerview)
        confirmButton = view.findViewById(R.id.button_confirm)

        val musics = loadSongsFromJSON()
        adapter = PlaylistMusicAdapter(musics)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        confirmButton.setOnClickListener {
            val selectedIds = adapter.getSelectedIds()
            onSongsSelected?.invoke(selectedIds) // 선택된 ID를 콜백으로 전달
            dismiss()
        }
    }

    private fun loadSongsFromJSON(): List<Song> {
        val songs = mutableListOf<Song>()
        try {
            val inputStream = requireContext().assets.open("songs.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val title = jsonObject.getString("title")
                val artist = jsonObject.getString("artist")
                songs.add(Song(id = id, title = title, artist=artist))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return songs
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                (resources.displayMetrics.heightPixels * 0.7).toInt()
            )
            setGravity(Gravity.CENTER)
        }
    }
}
