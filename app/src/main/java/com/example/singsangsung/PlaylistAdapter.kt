package com.example.singsangsung

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
class PlaylistAdapter(
    private val playlists: MutableList<Playlist>
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab2_playlist_element, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.textView.text = playlist.name
        val imageFile = File(holder.itemView.context.filesDir, playlist.imageName)
        if (imageFile.exists()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.absolutePath))
        }
    }

    override fun getItemCount(): Int = playlists.size
}

/*class PlaylistAdapter(
    private val playlists: MutableList<Playlist>
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    private var onItemClickListener: ((Playlist) -> Unit)? = null
    private var onItemLongClickListener: ((Playlist) -> Unit)? = null

    // ViewHolder 정의
    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_name)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(playlists[position])
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClickListener?.invoke(playlists[position])
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab2_playlist_element, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.textView.text = playlist.name

        // 이미지 파일 불러오기
        if (playlist.imageUrl.isNotEmpty()) {
            val file = File(holder.itemView.context.filesDir, playlist.imageUrl)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                holder.imageView.setImageBitmap(bitmap)
            } else {
                holder.imageView.setImageResource(android.R.color.darker_gray)
            }
        } else {
            holder.imageView.setImageResource(android.R.color.darker_gray)
        }
    }

    override fun getItemCount(): Int = playlists.size

    // 클릭 리스너 설정 메서드
    fun setOnItemClickListener(listener: (Playlist) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (Playlist) -> Unit) {
        onItemLongClickListener = listener
    }

    // 아이템 삭제
    fun removeItem(playlist: Playlist) {
        val position = playlists.indexOf(playlist)
        if (position != -1) {
            playlists.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}*/

/*package com.example.singsangsung

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.util.Base64
import java.io.File


class PlaylistAdapter(
    private val playlists: List<Playlist>
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab2_playlist_element, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.textView.text = playlist.name

        // 이미지 불러오기
        if (playlist.imageUrl.isNotEmpty()) {
            val file = File(holder.itemView.context.filesDir, playlist.imageUrl)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                holder.imageView.setImageBitmap(bitmap)
            } else {
                holder.imageView.setImageResource(android.R.color.darker_gray)
            }
        }
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            // 공백 및 줄바꿈 제거
            val cleanBase64 = base64String.replace("\\s".toRegex(), "")
            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("Base64 Decode Error", "Failed to decode Base64: ${e.message}")
            null
        }
    }



    override fun getItemCount(): Int = playlists.size
} */
//    private val playlists: List<Playlist>
//) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
//
//    // 뷰홀더 클래스 정의
//    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.item_image)
//        val textView: TextView = itemView.findViewById(R.id.item_name)
//    }
//
//    // 뷰홀더 생성
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.tab2_playlist_element, parent, false)
//        return PlaylistViewHolder(view)
//    }
//
//    // 뷰홀더에 데이터 바인딩
//    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
//        val playlist = playlists[position]
//        holder.textView.text = playlist.name
//
//        // Glide로 이미지 로드 (이미지 URL 사용)
//        Glide.with(holder.itemView.context)
//            .load(playlist.imageUrl)
//            .placeholder(android.R.color.darker_gray) // 로딩 중 표시할 색상
//            .into(holder.imageView)
//    }
//
//    // 데이터 아이템 수 반환
//    override fun getItemCount(): Int = playlists.size

