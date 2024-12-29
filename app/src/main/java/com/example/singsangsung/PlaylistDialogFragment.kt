package com.example.singsangsung

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PlaylistDialogFragment : DialogFragment() {

    private lateinit var manager: PlaylistPreferenceManager
    private lateinit var playlistName: EditText
    private lateinit var playlistImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var addButton: Button
    private lateinit var imagePreferenceManager: ImagePreferenceManager

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
        addButton = view.findViewById(R.id.button_add)

        imagePreferenceManager = ImagePreferenceManager(requireContext())
        manager = PlaylistPreferenceManager(requireContext())

        // 이미지 선택 버튼 클릭
        selectImageButton.setOnClickListener {
            showImagePickerDialog()
        }

        // 최종 추가 버튼 클릭
        addButton.setOnClickListener {
            val name = playlistName.text.toString().trim()
            if (name.isNotEmpty()) {
                val bitmap = (playlistImage.drawable as? BitmapDrawable)?.bitmap
                val imageName = bitmap?.let { imagePreferenceManager.saveImage(it) } ?: ""
                Log.d("heeju, image path", imagePreferenceManager.getImageFile(imageName).absolutePath)

                manager.addPlaylist(
                    Playlist(0, name, imageName)
                )
                Toast.makeText(requireContext(), "플레이리스트가 추가되었습니다! ${name} and ${manager.getPlaylists().get(0).imageName}", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                playlistName.error = "이름을 입력해주세요!"
            }
        }
    }

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
    /**
     * 이미지 선택 다이얼로그
     */
//    private fun showImagePickerDialog() {
//        val options = arrayOf("갤러리에서 선택", "사진 촬영")
//        android.app.AlertDialog.Builder(requireContext())
//            .setTitle("이미지 선택")
//            .setItems(options) { _, which ->
//                when (which) {
//                    0 -> checkPermissionAndOpenGallery()
//                    1 -> checkPermissionAndOpenCamera()
//                }
//            }.show()
//    }
//
//    /**
//     * 권한 확인 및 갤러리 열기
//     */
//    private fun checkPermissionAndOpenGallery() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                REQUEST_PERMISSION
//            )
//        } else {
//            openGallery()
//        }
//    }
//
//    /**
//     * 권한 확인 및 카메라 열기
//     */
//    private fun checkPermissionAndOpenCamera() {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.CAMERA),
//                REQUEST_PERMISSION
//            )
//        } else {
//            openCamera()
//        }
//    }
//
//    /**
//     * 갤러리 열기
//     */
//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }
//
//    /**
//     * 카메라 열기
//     */
//    private fun openCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, REQUEST_CAMERA)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_GALLERY -> {
//                    imageUri = data?.data
//                    playlistImage.setImageURI(imageUri)
//                }
//                REQUEST_CAMERA -> {
//                    imageBitmap = data?.extras?.get("data") as Bitmap
//                    playlistImage.setImageBitmap(imageBitmap)
//                }
//            }
//        }
//    }



//package com.example.singsangsung
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.BitmapDrawable
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.fragment.app.DialogFragment
//
//class PlaylistDialogFragment : DialogFragment() {
//
//    private lateinit var manager : PlaylistPreferenceManager
//    private lateinit var playlistName: EditText
//    private lateinit var playlistImage: ImageView
//    private lateinit var selectImageButton: Button
//    private lateinit var addButton: Button
//    private lateinit var imagePreferenceManager: ImagePreferenceManager
//
//    private var imageUri: Uri? = null
//    private var imageBitmap: Bitmap? = null
//
//    companion object {
//        private const val REQUEST_GALLERY = 1001
//        private const val REQUEST_CAMERA = 1002
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.create_playlist_page, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        playlistName = view.findViewById(R.id.playlist_name)
//        playlistImage = view.findViewById(R.id.playlist_image)
//        selectImageButton = view.findViewById(R.id.playlist_addBtn)
//        addButton = view.findViewById(R.id.button_add)
//
//
//        imagePreferenceManager = ImagePreferenceManager(requireContext())
//        manager = PlaylistPreferenceManager(requireContext())
//
//        selectImageButton.setOnClickListener {
//            showImagePickerDialog()
//        }
//
//        addButton.setOnClickListener {
//            val name = playlistName.text.toString().trim()
//            if (name.isNotEmpty()) {
//                val bitmap = (playlistImage.drawable as? BitmapDrawable)?.bitmap
//                val imageName = bitmap?.let { imagePreferenceManager.saveImage(it) } ?: ""
//
//                manager.addPlaylist(
//                    Playlist(0, name, imageName)
//                )
//                dismiss()
//            } else {
//                Toast.makeText(requireContext(), "이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
//            }
//        }
//
////        playlistImage.setOnClickListener {
////            openGallery()
////        }
//
////        addButton.setOnClickListener {
////            val name = playlistName.text.toString()
////            var imageUriFinal = ""
////
////            val imageToBitmap: Bitmap? = (playlistImage.drawable as? BitmapDrawable)?.bitmap
////            if (imageToBitmap != null) {
////                try {
////                    val imageUri = imagePreferenceManager.saveImage(imageToBitmap)
////                    imageUriFinal = imageUri
////                    Log.d("Image Save", "Saved image URI: $imageUriFinal")
////                } catch (e: Exception) {
////                    Log.e("Image Save", "Failed to save image: ${e.message}")
////                }
////            } else {
////                Log.e("Image Save", "Failed to convert drawable to Bitmap")
////            }
////
////            if (name.isNotEmpty()) {
////                val playlist = Playlist(0,name, imageUriFinal)
////                manager.addPlaylist(playlist)
////                Toast.makeText(requireContext(), "플레이리스트가 추가되었습니다!", Toast.LENGTH_SHORT).show()
////
//////                val playlists = manager.getPlaylists()
//////                for (p in playlists) {
//////                    Log.d("Playlist Info", "${p.name} name -----------------and image is -----------------${p.imageUrl}")
//////                }
////                dismiss()
////            } else {
////                playlistName.error = "플레이리스트 이름을 입력해주세요"
////            }
////        }
//
//
//
//    }
//
//    private fun showImagePickerDialog() {
//        val options = arrayOf("갤러리에서 선택", "사진 촬영")
//        android.app.AlertDialog.Builder(requireContext())
//            .setTitle("이미지 선택")
//            .setItems(options) { _, which ->
//                when (which) {
//                    0 -> openGallery()
//                    1 -> openCamera()
//                }
//            }.show()
//    }
//
//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }
//
//    private fun openCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, REQUEST_CAMERA)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_GALLERY -> {
//                    imageUri = data?.data
//                    playlistImage.setImageURI(imageUri)
//                }
//                REQUEST_CAMERA -> {
//                    imageBitmap = data?.extras?.get("data") as Bitmap
//                    playlistImage.setImageBitmap(imageBitmap)
//                }
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.apply {
//            // Dialog 높이와 너비 설정
//            setLayout(
//                (resources.displayMetrics.widthPixels * 0.8).toInt(), // 너비 (화면의 90%)
//                (resources.displayMetrics.heightPixels * 0.7).toInt() // 높이 (화면의 70%)
//            )
//            setGravity(Gravity.CENTER) // 중앙 정렬
//        }
//    }
//
//    interface OnDismissListener {
//        fun onDismiss()
//    }
//
//    private var onDismissListener: OnDismissListener? = null
//
//    fun setOnDismissListener(listener: OnDismissListener) {
//        onDismissListener = listener
//    }
//
//    // 다이얼로그 종료 시 호출
//    override fun dismiss() {
//        super.dismiss()
//        onDismissListener?.onDismiss()
//    }
//
//
//}
/*package com.example.singsangsung

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import java.io.File
import java.io.FileOutputStream

class PlaylistDialogFragment : DialogFragment() {

    private lateinit var playlistName: EditText
    private lateinit var playlistImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var addButton: Button
    private lateinit var playlistPreferenceManager: PlaylistPreferenceManager

    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null

    companion object {
        private const val REQUEST_GALLERY = 1001
        private const val REQUEST_CAMERA = 1002
    }

    interface OnPlaylistAddedListener {
        fun onPlaylistAdded(playlist: Playlist)
    }

    private var listener: OnPlaylistAddedListener? = null

    fun setOnPlaylistAddedListener(listener: OnPlaylistAddedListener) {
        this.listener = listener
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
        addButton = view.findViewById(R.id.button_add)

        playlistPreferenceManager = PlaylistPreferenceManager(requireContext())

        selectImageButton.setOnClickListener {
            showImagePickerDialog()
        }

        addButton.setOnClickListener {
            savePlaylist()


        }
    }

    private fun savePlaylist() {
        val name = playlistName.text.toString().trim()
        if (name.isEmpty()) {
            playlistName.error = "플레이리스트 이름을 입력해주세요"
            return
        }

        // 이미지 저장
        var imageFileName = ""
        val bitmap = (playlistImage.drawable as? BitmapDrawable)?.bitmap
        if (bitmap != null) {
            imageFileName = saveImageToInternalStorage(bitmap, name)
        }

        val internalFile = File(requireContext().filesDir, "playlist_$name.jpg")
        val externalFile = File(requireContext().getExternalFilesDir(null), "playlist_$name.jpg")

        Log.d("File Debug", "Internal Path: ${internalFile.absolutePath}")
        Log.d("File Debug", "External Path: ${externalFile.absolutePath}")
        Log.d("File Debug", "Internal File Exists: ${internalFile.exists()}")
        Log.d("File Debug", "External File Exists: ${externalFile.exists()}")

        // SharedPreferences에 저장
        val playlist = Playlist(0, name, imageFileName)
        playlistPreferenceManager.addPlaylist(playlist)

        listener?.onPlaylistAdded(playlist)
        dismiss()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap, name: String): String {
        val fileName = "playlist_$name.jpg"
        try {
            val file = File(requireContext().filesDir, fileName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            Log.d("Image location", "image saved in : ${file.path}")
            Log.d("Image Save", "Image saved: $fileName")
            return fileName
        } catch (e: Exception) {
            Log.e("Image Save", "Failed: ${e.message}")
        }
        return ""
    }

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
}

//class PlaylistDialogFragment : DialogFragment() {
//
//    private lateinit var playlistName: EditText
//    private lateinit var playlistImage: ImageView
//    private lateinit var selectImageButton: Button
//    private lateinit var addButton: Button
//    private lateinit var imagePreferenceManager: ImagePreferenceManager
//    private lateinit var playlistPreferenceManager: PlaylistPreferenceManager
//
//    private var imageUri: Uri? = null
//    private var imageBitmap: Bitmap? = null
//
//    companion object {
//        private const val REQUEST_GALLERY = 1001
//        private const val REQUEST_CAMERA = 1002
//    }
//
//    interface OnPlaylistAddedListener {
//        fun onPlaylistAdded(playlist: Playlist)
//    }
//
//    private var listener: OnPlaylistAddedListener? = null
//
//    fun setOnPlaylistAddedListener(listener: OnPlaylistAddedListener) {
//        this.listener = listener
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.create_playlist_page, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        playlistName = view.findViewById(R.id.playlist_name)
//        playlistImage = view.findViewById(R.id.playlist_image)
//        selectImageButton = view.findViewById(R.id.playlist_addBtn)
//        addButton = view.findViewById(R.id.button_add)
//
//        imagePreferenceManager = ImagePreferenceManager(requireContext())
//        playlistPreferenceManager = PlaylistPreferenceManager(requireContext())
//
//        selectImageButton.setOnClickListener {
//            showImagePickerDialog()
//        }
//
//        addButton.setOnClickListener {
//            val name = playlistName.text.toString()
//            var imageUriFinal = ""
//
//            val imageToBitmap: Bitmap? = (playlistImage.drawable as? BitmapDrawable)?.bitmap
//            if (imageToBitmap != null) {
//                try {
//                    val imageUri = imagePreferenceManager.saveImage(imageToBitmap)
//                    imageUriFinal = imageUri
//                    Log.d("Image Save", "Saved image URI: $imageUriFinal")
//                } catch (e: Exception) {
//                    Log.e("Image Save", "Failed to save image: ${e.message}")
//                }
//            } else {
//                Log.e("Image Save", "Failed to convert drawable to Bitmap")
//            }
//
//            if (name.isNotEmpty()) {
//                val playlist = Playlist(0, name, imageUriFinal)
//                playlistPreferenceManager.addPlaylist(playlist)
//                listener?.onPlaylistAdded(playlist) // 콜백 호출
//                dismiss()
//            } else {
//                playlistName.error = "플레이리스트 이름을 입력해주세요"
//            }
//        }
//    }
//
//    private fun showImagePickerDialog() {
//        val options = arrayOf("갤러리에서 선택", "사진 촬영")
//        android.app.AlertDialog.Builder(requireContext())
//            .setTitle("이미지 선택")
//            .setItems(options) { _, which ->
//                when (which) {
//                    0 -> openGallery()
//                    1 -> openCamera()
//                }
//            }.show()
//    }
//
//    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, REQUEST_GALLERY)
//    }
//
//    private fun openCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, REQUEST_CAMERA)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_GALLERY -> {
//                    imageUri = data?.data
//                    playlistImage.setImageURI(imageUri)
//                }
//                REQUEST_CAMERA -> {
//                    imageBitmap = data?.extras?.get("data") as Bitmap
//                    playlistImage.setImageBitmap(imageBitmap)
//                }
//            }
//        }
//    }
//}
*/