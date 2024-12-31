package com.example.singsangsung.PlayList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.singsangsung.R
import com.example.singsangsung.model.Song

class PlaylistMusicAdapter(
    private val items: List<Song>
) : RecyclerView.Adapter<PlaylistMusicAdapter.ViewHolder>() {

    private val selectedIds = mutableSetOf<Int>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.music_name)
        val checkBox: CheckBox = itemView.findViewById(R.id.music_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = "${item.title} - ${item.artist}"
        //holder.checkBox.isChecked = item.isChecked

        // 체크박스 상태 변경 리스너
        holder.checkBox.setOnCheckedChangeListener(null) // 리스너 초기화
        holder.checkBox.isChecked = item.isChecked
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
            if (isChecked) {
                selectedIds.add(item.id)
            } else {
                selectedIds.remove(item.id)
            }
        }

        // 아이템 뷰 클릭 시 체크박스 상태 변경
        holder.itemView.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }
    }

    override fun getItemCount(): Int = items.size

    // 선택된 ID 반환
    fun getSelectedIds(): List<Int> {
        return items.filter { it.isChecked }.map { it.id }
    }
}

