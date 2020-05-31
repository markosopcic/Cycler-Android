package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.adapters.EventsPagerAdapter
import com.markosopcic.cycler.view.adapters.InvitationsPagerAdapter
import kotlinx.android.synthetic.main.events_fragment.*
import kotlinx.android.synthetic.main.fragment_invitations.*

class EventsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.events_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter = EventsPagerAdapter(childFragmentManager)
        events_view_pager.adapter = fragmentAdapter
        events_tab_layout.setupWithViewPager(events_view_pager)
    }
}
