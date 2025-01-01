package com.example.singsangsung.PlayList

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.singsangsung.R
import com.example.singsangsung.model.Song
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class Tab1AddplaylistDialogFragment : DialogFragment() {
    private lateinit var songName: EditText
    private lateinit var artistName: EditText
    private lateinit var addSongBtn: Button
    private lateinit var songManager: SongPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab1_add_playlist_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songName = view.findViewById(R.id.tab1_add_songName)
        artistName = view.findViewById(R.id.tab1_add_artistName)
        addSongBtn = view.findViewById(R.id.tab1_addSongBtn)
        songManager = SongPreferenceManager(requireContext())

        addSongBtn.setOnClickListener {
            val name = songName.text.toString().trim()
            val artist = artistName.text.toString().trim()
            if (name.isNotEmpty() && artist.isNotEmpty()) {
                val songData = findSongInAssets(name, artist)

                if (songData != null) {
                    val newSong = Song(
                        id = 0, // ID는 SongPreferenceManager에서 자동으로 부여됨
                        title = name,
                        artist = artist,
                        duration = songData.getString("duration"),
                        imageUrl = songData.getString("image_url")
                    )

                    songManager.addSong(newSong)
                    dismiss()
                } else {
                    val newSong = Song(
                        id = 0, // ID는 SongPreferenceManager에서 자동으로 부여됨
                        title = name,
                        artist = artist,
                        duration = "3:00", // Default
                        imageUrl = "https://example.com/default_image.jpg"
                    )

                    songManager.addSong(newSong)

                    dismiss()
                }
                Toast.makeText(requireContext(), "노래가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findSongInAssets(name: String, artist: String): JSONObject? {
        val jsonString = loadJSONFromAsset("songsDB.json") ?: return null
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val song = jsonArray.getJSONObject(i)
            if (song.getString("title") == name && song.getString("artist") == artist) {
                return song
            }
        }

        return null
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        return try {
            val inputStream = requireContext().assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                (resources.displayMetrics.heightPixels * 0.6).toInt()
            )
            setGravity(Gravity.CENTER)
        }
    }

    /**
     * 리스너
     */
    private var onDismissListener: OnDismissListener? = null

    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    interface OnDismissListener {
        fun onDismiss()
    }

    override fun dismiss() {
        super.dismiss()
        onDismissListener?.onDismiss()
    }
}
