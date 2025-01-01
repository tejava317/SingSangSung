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

class Tab1AddplaylistDialogFragment : DialogFragment() {
    private lateinit var songName: EditText
    private lateinit var artistName: EditText
    private lateinit var addSongBtn: Button
    private lateinit var songManager : SongPreferenceManager
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

        //manager = PlaylistPreferenceManager(requireContext())

        // 이미지 선택 버튼 클릭 : 이미지 선택화면으로 갤러리이동

        addSongBtn.setOnClickListener {
            val name = songName.text.toString().trim()
            val artist = artistName.text.toString().trim()
            if (name.isNotEmpty() && artist.isNotEmpty()) {
                val newSong = Song(
                    id = 0, // ID는 SongPreferenceManager에서 자동으로 부여됨
                    title = name,
                    artist = artist,
                    duration = "3:00", // 기본 값
                    imageUrl = "https://example.com/default_image.jpg"
                )

                songManager.addSong(newSong)

                Toast.makeText(requireContext(), "노래가 추가되었습니다!", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }


    }


    // 이미지 올릴때 갤러리에서 가져오기 팝업


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
//    interface OnDismissListener {
//        fun onDismiss()
//    }
//
    private var onDismissListener: OnDismissListener? = null
//
//    fun setOnDismissListener(listener: () -> Unit) {
//        onDismissListener = listener
//    }
//
//    override fun dismiss() {
//        super.dismiss()
//        onDismissListener?.onDismiss()
//    }

    interface OnDismissListener {
        fun onDismiss()
    }

    // 📌 OnDismissListener 설정
    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    override fun dismiss() {
        super.dismiss()
        onDismissListener?.onDismiss()
    }
}