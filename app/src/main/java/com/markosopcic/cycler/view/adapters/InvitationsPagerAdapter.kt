package com.markosopcic.cycler.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.markosopcic.cycler.view.fragments.EventInvitationsFragment
import com.markosopcic.cycler.view.fragments.FriendRequestsFragment
import com.markosopcic.cycler.view.fragments.FriendsFragment

class InvitationsPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm){

    lateinit var friendRequestsFragment: FriendRequestsFragment
    lateinit var eventInvitationsFragment: EventInvitationsFragment
    lateinit var friendsFragment:FriendsFragment

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->{
                if(!::friendsFragment.isInitialized){
                    friendsFragment =  FriendsFragment()
                }
                friendsFragment

            }
            1 -> {
                if(!::friendRequestsFragment.isInitialized){
                    friendRequestsFragment =  FriendRequestsFragment()
                }
                friendRequestsFragment
            }
            2 -> {
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
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){

            0 -> "Friends"
            1 -> "Friend requests"
            2 -> "Event invitations"
            else->{
                "Friends"
            }
        }
    }

}