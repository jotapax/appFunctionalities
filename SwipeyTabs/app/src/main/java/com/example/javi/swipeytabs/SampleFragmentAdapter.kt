package com.example.javi.swipeytabs

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SampleFragmentAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return Fragment1()
        } else {
            return Fragment2()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return "Tab 1"
        } else {
            return "Tab 2"
        }
    }

}