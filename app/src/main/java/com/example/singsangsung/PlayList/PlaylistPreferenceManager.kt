package com.example.singsangsung.PlayList

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.singsangsung.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistPreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("playlist_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PLAYLIST_KEY = "playlist_key"
        private const val LAST_ID_KEY = "last_playlist_id"
    }

    // 📌 모든 플레이리스트 불러오기
    fun getPlaylists(): List<Playlist> {
        val json = prefs.getString(PLAYLIST_KEY, null)
        return try {
            if (json != null) {
                val type = object : TypeToken<List<Playlist>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PlaylistPref", "Failed to parse playlists: ${e.message}")
            emptyList()
        }
    }

    // 📌 플레이리스트 저장
    fun savePlaylists(playlists: List<Playlist>) {
        val json = gson.toJson(playlists)
        prefs.edit().putString(PLAYLIST_KEY, json).apply()
    }

    // 📌 플레이리스트 추가
    fun addPlaylist(playlist: Playlist) {
        val playlistList = getPlaylists().toMutableList()
        val newPlaylist = playlist.copy(id = getNextId())
        playlistList.add(newPlaylist)
        savePlaylists(playlistList)
    }

    private fun getNextId(): Int {
        val lastId = prefs.getInt(LAST_ID_KEY, 0)
        val nextId = lastId + 1
        prefs.edit().putInt(LAST_ID_KEY, nextId).apply()
        return nextId
    }

    // 📌 마지막 ID 업데이트
    fun updateLastId(newLastId: Int) {
        prefs.edit().putInt(LAST_ID_KEY, newLastId).apply()
    }
}

