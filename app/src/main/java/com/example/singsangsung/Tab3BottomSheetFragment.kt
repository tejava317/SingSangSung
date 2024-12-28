package com.example.singsangsung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Tab3BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃 연결
        return inflater.inflate(R.layout.tab_layout3_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // '닫기' 버튼
        val closeButton = view.findViewById<Button>(R.id.bottomSheetCloseButton)
        closeButton.setOnClickListener {
            dismiss() // Bottom Sheet 닫기
        }

        // 'My Playlist로 이동' 버튼
        val myPlaylistButton = view.findViewById<Button>(R.id.myPlaylistButton)
        myPlaylistButton.setOnClickListener {
            // ViewPager2의 두 번째 탭으로 이동
            val activity = activity as? MainActivity
            activity?.let {
                it.viewPager.currentItem = 1 // 두 번째 탭 (인덱스 1)
            }
            dismiss() // Bottom Sheet 닫기
        }
    }
}