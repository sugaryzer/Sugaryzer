package com.sugaryzer.sugaryzer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sugaryzer.sugaryzer.ui.history.HistoryFragment
import com.sugaryzer.sugaryzer.ui.home.HomeFragment
import com.sugaryzer.sugaryzer.ui.profile.ProfileFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = HistoryFragment()
            2 -> fragment = ProfileFragment()
        }
        return fragment as Fragment
    }
}