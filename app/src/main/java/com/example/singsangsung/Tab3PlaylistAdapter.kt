package com.example.singsangsung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singsangsung.model.Playlist

class PlaylistAdapter(
    private val playlist: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.playlistImage)
        val nameTextView: TextView = view.findViewById(R.id.playlistName)

        init {
            view.setOnClickListener {
                onItemClick(playlist[adapterPosition]) // 클릭된 Playlist 객체 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab_layout3_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = playlist[position]
        holder.nameTextView.text = item.name
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount() = playlist.size
}