package com.example.singsangsung;

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.lang.IllegalArgumentException

class ViewPagerAdapter(@NonNull fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position:Int) : Fragment {
        return when (position){
            0-> Tab1Fragment();
            1 -> Tab2Fragment();
            2 -> Tab3Fragment();
            else -> throw IllegalArgumentException("Invalid position : $position")
            // when문은 가능한 모든 경우를 다뤄야함
        }
    }

    override fun getItemCount() : Int {
        return 3;
    }
}
