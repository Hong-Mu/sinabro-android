package com.witchapps.sinabro.ui.library

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.witchapps.sinabro.ui.library.bookmark.LibBookmarkFragment
import com.witchapps.sinabro.ui.library.collection.LibCollectionFragment

class LibraryViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 1 // 2

    override fun createFragment(position: Int) = when(position) {
        0 -> LibBookmarkFragment()
        else -> LibCollectionFragment()
    }
}