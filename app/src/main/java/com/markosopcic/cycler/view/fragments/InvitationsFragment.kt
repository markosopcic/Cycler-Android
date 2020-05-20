package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.adapters.InvitationsPagerAdapter
import kotlinx.android.synthetic.main.fragment_invitations.*


class InvitationsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_invitations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = InvitationsPagerAdapter(childFragmentManager)
        invitations_view_pager.adapter = fragmentAdapter
        invitations_tab_layout.setupWithViewPager(invitations_view_pager)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

}
