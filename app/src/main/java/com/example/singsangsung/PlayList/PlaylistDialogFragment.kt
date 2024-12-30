package com.example.singsangsung.PlayList

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.singsangsung.Playlist
import com.example.singsangsung.R

class PlaylistDialogFragment : DialogFragment() {

    private lateinit var manager: PlaylistPreferenceManager
    private lateinit var playlistName: EditText
    private lateinit var playlistImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var addButton: Button
    private lateinit var imagePreferenceManager: ImagePreferenceManager
    private lateinit var songBtn : Button
    private lateinit var musicList : List<Int>

    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null

    companion object {
        private const val REQUEST_GALLERY = 1001
        private const val REQUEST_CAMERA = 1002
        private const val REQUEST_PERMISSION = 1003
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_playlist_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistName = view.findViewById(R.id.playlist_name)
        playlistImage = view.findViewById(R.id.playlist_image)
        selectImageButton = view.findViewById(R.id.playlist_addBtn)
        songBtn = view.findViewById(R.id.playlist_songBtn)
        addButton = view.findViewById(R.id.button_add)

        imagePreferenceManager = ImagePreferenceManager(requireContext())
        manager = PlaylistPreferenceManager(requireContext())

        // 이미지 선택 버튼 클릭 : 이미지 선택화면으로 갤러리이동
        selectImageButton.setOnClickListener {
            showImagePickerDialog()
        }

        // 최종 추가 버튼 : 데이터 처리
        addButton.setOnClickListener {
            val name = playlistName.text.toString().trim()
            if (name.isNotEmpty()) {
                val bitmap = (playlistImage.drawable as? BitmapDrawable)?.bitmap
                val imageName = bitmap?.let { imagePreferenceManager.saveImage(it) } ?: ""
                manager.addPlaylist(
                    Playlist(
                        id = System.currentTimeMillis().toInt(),
                        name = name,
                        imageName = imageName,
                        checkedMusic = musicList
                    )
                )
                dismiss()
            } else {
                playlistName.error = "이름을 입력해주세요!"
            }
        }

        // 플레이리스트에 넣을 노래 추가 다이얼로그 띄우기 : 성공
        songBtn.setOnClickListener {
            val musicDialog = PlaylistMusicFragment()
//            musicDialog.onSongsSelected = { selectedIds ->
//                musicList = selectedIds
//                Toast.makeText(requireContext(), "선택된 ID: $selectedIds", Toast.LENGTH_SHORT).show()
//            }
            musicDialog.show(parentFragmentManager, "PlaylistMusicDialog")
        }
    }
    // 플레이리스트 추가할때 화면 띄우기
//    private fun openPlaylistMusicDialog(){
//
//            // 음악체크화면이 닫혔을때 발생할 이벤트 - onDismissListener : 다이얼로그가 닫힐때 호출되는 인터페이스
//            checkResult.setOnDismissListener(object : PlaylistMusicFragment.OnDismissListener {
//            override fun onDismiss() {
//                // musiclist에 추가
//                musicList.
//            }
//        })
//    }

    // 이미지 올릴때 갤러리에서 가져오기 팝업
    private fun showImagePickerDialog() {
        val options = arrayOf("갤러리에서 선택", "사진 촬영")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("이미지 선택")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
            }.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    imageUri = data?.data
                    playlistImage.setImageURI(imageUri)
                }
                REQUEST_CAMERA -> {
                    imageBitmap = data?.extras?.get("data") as Bitmap
                    playlistImage.setImageBitmap(imageBitmap)
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                (resources.displayMetrics.heightPixels * 0.7).toInt()
            )
            setGravity(Gravity.CENTER)
        }
    }

    /**
     * 리스너
     */
    interface OnDismissListener {
        fun onDismiss()
    }

    private var onDismissListener: OnDismissListener? = null

    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    override fun dismiss() {
        super.dismiss()
        onDismissListener?.onDismiss()
    }

}
