package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.adapters.InvitationsPagerAdapter
import kotlinx.android.synthetic.main.fragment_invitations.*


class SocialFragment : Fragment() {


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


}
