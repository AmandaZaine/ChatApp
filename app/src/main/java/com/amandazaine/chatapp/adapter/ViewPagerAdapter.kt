package com.amandazaine.chatapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.amandazaine.chatapp.fragment.ChatsFragment
import com.amandazaine.chatapp.fragment.ContactsFragment

class ViewPagerAdapter(
    private val tabTitles: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return ChatsFragment()
            1 -> return ContactsFragment()
        }

        return ChatsFragment()
    }

    override fun getItemCount(): Int {
        return tabTitles.size
    }

}