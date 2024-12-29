//package com.example.singsangsung
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class GridRecyclerAdapter(
//    private val items: List<Playlist>
//) : RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.item_image)
//        val textView: TextView = itemView.findViewById(R.id.item_name)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.tab2_playlist_element, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
//        holder.imageView.setImageResource(item.id)
//        holder.textView.text = item.name
//    }
//
//    override fun getItemCount(): Int = items.size
//}
package com.example.singsangsung

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        val imageFile = File(holder.itemView.context.filesDir, item.imageName)
        if (imageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        } else {
            holder.imageView.setImageResource(android.R.color.darker_gray)
        }
    }

    override fun getItemCount(): Int = items.size
}
