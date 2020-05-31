package com.markosopcic.cycler.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.markosopcic.cycler.view.fragments.EventsViewFragment
import com.markosopcic.cycler.view.fragments.NewEventFragment

class EventsPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    val eventsViewFragment = EventsViewFragment()
    val newEventFragment = NewEventFragment()

    override fun getItem(position: Int): Fragment {
        when(position){
            1 -> return newEventFragment
            else -> return eventsViewFragment
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            1 -> {return "Create new Event"
            }
            else -> {
                return "Own events"
            }
        }
    }
}