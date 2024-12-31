package com.example.singsangsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;

class Tab3Fragment : Fragment() {
    override fun onCreateView(inflater : LayoutInflater , container : ViewGroup? , savedInstanceState: Bundle?) : View? {
        return inflater.inflate(R.layout.tab_layout3, container, false);
    }

    // 왜 R만 써도 인식되는건지 확인
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FrameLayout 클릭 리스너 추가
        val frameLayout = view.findViewById<FrameLayout>(R.id.tab3_start_button)
        frameLayout.setOnClickListener {
            // BottomSheetFragment 표시
            val bottomSheet = Tab3BottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "Tab3BottomSheet")
        }
    }
}
