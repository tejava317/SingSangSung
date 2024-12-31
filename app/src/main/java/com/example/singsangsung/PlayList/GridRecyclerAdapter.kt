
package com.example.singsangsung.PlayList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singsangsung.R
import com.example.singsangsung.model.Playlist
import java.io.File
class GridRecyclerAdapter(
    private val items: List<Playlist>
) : RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tab2_playlist_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name

        // 이미지 파일 로드
        val imageFile = File(holder.itemView.context.filesDir, item.imageName)
        if (imageFile.exists()) {
            Glide.with(holder.itemView.context)
                .load(imageFile)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(android.R.color.darker_gray)
        }
    }

    override fun getItemCount(): Int = items.size
}
