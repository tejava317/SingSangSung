package com.example.singsangsung;

import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView

import androidx.fragment.app.Fragment;
import org.json.JSONArray

class Tab1Fragment : Fragment() {


    // XML 레이아웃을 inflate해서 fragment 뷰 계층 구조 생성
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        // XML 레이아웃 파일 inflate하는 코드
        return inflater.inflate(R.layout.tab_layout1, container, false);
    }

    // 뷰가 생성되고 뷰에 대한 추가작업 - 텍스트뷰에 데이터 설정
    override fun onViewCreated(view:View, savedInstanceState:Bundle?){
        super.onViewCreated(view, savedInstanceState);

        val textView : TextView = view.findViewById(R.id.tab1TextView)

        try{
            // assets에서 JSON 파일 읽기
            val jsonStr = requireContext().assets.open("songs.json").reader().readText()

            // json 배열 파싱
            val jsonArray = JSONArray(jsonStr)

            // TextView에 표시
            for (i in 0 until jsonArray.length()) {
                // 데이터 값이 저장된 jsosnArray의 각 i번째 값
                val jsonObject = jsonArray.getJSONObject(i)
                textView.append("---")

                val songName = jsonObject.getString("title")
                val artist = jsonObject.getString("artist")
                val duration = jsonObject.getString("duration")
                val songImage = jsonObject.getString("image_url")

                textView.append("$songName \n")
                textView.append("$artist - $duration\n")

            }
        } catch (e : Exception){
            print(e.message)
        }

    }

}
