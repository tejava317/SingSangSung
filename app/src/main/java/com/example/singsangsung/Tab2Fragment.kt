package com.example.singsangsung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

class Tab2Fragment : Fragment() {

    override fun onCreateView(inflater : LayoutInflater , container : ViewGroup? , savedInstanceState: Bundle?) : View {
        return inflater.inflate(R.layout.tab_layout2, container, false);
    }

    // 왜 R만 써도 인식되는건지 확인
    override fun onViewCreated(view:View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState);
    }
}
