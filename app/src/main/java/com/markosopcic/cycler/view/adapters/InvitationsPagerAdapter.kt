package com.markosopcic.cycler.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.markosopcic.cycler.view.fragments.EventInvitationsFragment
import com.markosopcic.cycler.view.fragments.FriendRequestsFragment

class InvitationsPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){

    lateinit var friendRequestsFragment: FriendRequestsFragment
    lateinit var eventInvitationsFragment: EventInvitationsFragment

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->{
                if(!::friendRequestsFragment.isInitialized){
                    friendRequestsFragment =  FriendRequestsFragment()
                }
                friendRequestsFragment

            }
            1 -> {
                if(!::eventInvitationsFragment.isInitialized){
                    eventInvitationsFragment =  EventInvitationsFragment()
                }
                eventInvitationsFragment
            }
            else ->{
                if(!::friendRequestsFragment.isInitialized){
                    friendRequestsFragment =  FriendRequestsFragment()
                }
                friendRequestsFragment

            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Friend Requests"
            1 -> "Event invitations"
            else->{
                "Friend Requests"
            }
        }
    }

}