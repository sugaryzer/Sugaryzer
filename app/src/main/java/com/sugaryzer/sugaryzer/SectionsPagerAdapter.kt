package com.sugaryzer.sugaryzer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sugaryzer.sugaryzer.ui.history.HistoryFragment
import com.sugaryzer.sugaryzer.ui.home.HomeFragment
import com.sugaryzer.sugaryzer.ui.news.NewsFragment
import com.sugaryzer.sugaryzer.ui.profile.ProfileFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val fragments = listOf(HomeFragment(), HistoryFragment(), NewsFragment(), ProfileFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}