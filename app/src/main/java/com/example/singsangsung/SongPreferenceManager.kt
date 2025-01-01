package com.example.singsangsung.PlayList

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.singsangsung.model.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class SongPreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("song_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val SONGS_KEY = "songs_key"
        private const val LAST_ID_KEY = "last_song_id"
    }

    // 📌 모든 노래 불러오기
    fun getSongs(): List<Song> {
        val json = prefs.getString(SONGS_KEY, null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<Song>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SongPref", "Failed to parse songs: ${e.message}")
            emptyList()
        }
    }

    // 📌 노래 저장
    private fun saveSongs(songs: List<Song>) {
        val json = gson.toJson(songs)
        prefs.edit().putString(SONGS_KEY, json).apply()
    }

    // 📌 노래 추가
    fun addSong(song: Song) {
        val songList = getSongs().toMutableList()
        val newSong = song.copy(id = getNextId())
        songList.add(newSong)
        saveSongs(songList)
    }

    // 📌 모든 노래 삭제
    fun clearSongs() {
        saveSongs(emptyList())
        prefs.edit().putInt(LAST_ID_KEY, 0).apply()
    }

    // 📌 노래 목록 교체
    fun setSongs(songs: List<Song>) {
        saveSongs(songs)
    }

    // 📌 여러 노래 추가
    fun addAllSongs(newSongs: List<Song>) {
        val songList = getSongs().toMutableList()
        songList.addAll(newSongs)
        saveSongs(songList)
    }

    // 📌 ID 자동 증가
    private fun getNextId(): Int {
        val lastId = prefs.getInt(LAST_ID_KEY, 0)
        val nextId = lastId + 1
        prefs.edit().putInt(LAST_ID_KEY, nextId).apply()
        return nextId
    }

    fun initializeFromJson(context: Context) {
        try {
            val inputStream = context.assets.open("songs.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            val songList = mutableListOf<Song>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val title = jsonObject.getString("title")
                val artist = jsonObject.getString("artist")
                val duration = jsonObject.getString("duration")
                val imageUrl = jsonObject.getString("image_url")

                songList.add(Song(id, title, artist, duration, imageUrl))
            }

            saveSongs(songList)
            prefs.edit().putInt(LAST_ID_KEY, songList.size).apply()
            Log.d("SongPref", "JSON 데이터를 SharedPreferences로 성공적으로 이전했습니다.")
        } catch (e: Exception) {
            Log.e("SongPref", "Failed to initialize from JSON: ${e.message}")
        }
    }
}
