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

        // 닫기 버튼 클릭 리스너 설정
        val closeButton = view.findViewById<Button>(R.id.bottomSheetCloseButton)
        closeButton.setOnClickListener {
            dismiss() // Bottom Sheet만 닫음
        }
    }
}
