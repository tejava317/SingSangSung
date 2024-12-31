package com.example.singsangsung.PlayList

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.singsangsung.R
import java.io.File

class PlaylistDetailFragmentPage : Fragment() {

    private var playlistId: Int? = null
    private var playlistName: String? = null
    private var playlistImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getInt("playlistId")
            playlistName = it.getString("playlistName")
            playlistImage = it.getString("playlistImage")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.tab2_playlist_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView: ImageView = view.findViewById(R.id.tab2_playlist_detail_image)
        val nameTextView: TextView = view.findViewById(R.id.tab2_playlist_detail_plyName)

        nameTextView.text = playlistName
        // 이미지 표시 (간단한 이미지 경로 설정 예제)
        if (!playlistImage.isNullOrEmpty()) {
            val imageFile = File(requireContext().filesDir, playlistImage!!)
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                imageView.setImageBitmap(bitmap)
            }
        }
    }


}
