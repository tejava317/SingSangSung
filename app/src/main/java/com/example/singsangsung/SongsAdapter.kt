import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singsangsung.R
import com.example.singsangsung.model.Song


class SongsAdapter(var songs: List<Song>) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    // 뷰홀더 정의
    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val artistAndDuration: TextView = view.findViewById(R.id.artist_and_duration)
        val albumImage: ImageView = view.findViewById(R.id.album_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title
        holder.artistAndDuration.text = "${song.artist} • ${song.duration}"

        // Glide를 사용하여 이미지 로드
        Glide.with(holder.albumImage.context)
            .load(song.imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.albumImage)
    }

    override fun getItemCount(): Int = songs.size

    // 📌 노래 목록 업데이트 (SharedPreferences에서 최신 목록 반영)
    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}

