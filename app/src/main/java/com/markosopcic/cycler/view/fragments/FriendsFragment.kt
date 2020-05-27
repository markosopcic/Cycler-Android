package com.markosopcic.cycler.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.UserDetails
import com.markosopcic.cycler.network.models.UserSearchResult
import com.markosopcic.cycler.view.adapters.FriendsAdapter
import com.markosopcic.cycler.viewmodel.FriendsViewModel
import kotlinx.android.synthetic.main.friends_fragment.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

class FriendsFragment : Fragment() {

    var viewModel = get<FriendsViewModel>()
    var friendsAdapter = FriendsAdapter(::displayUserDetails)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friends_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(this@FriendsFragment.activity)
        manager.stackFromEnd = false
        friends_recycler_view.layoutManager = manager
        friends_recycler_view.adapter = friendsAdapter


        viewModel.currentUsers.observe(viewLifecycleOwner, Observer {
            friendsAdapter.changeDataset(it as ArrayList<UserSearchResult>)
        })
        people_search.doOnTextChanged { text, start, count, after ->
            viewModel.searchInput.value = text.toString()
          }

       people_search.addTextChangedListener {
            val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

            var searchJob: Job? = null

               searchJob?.cancel()
               searchJob = coroutineScope.launch {
                   it.toString().let {
                       delay(500)
                       if (it.isEmpty() || it.length < 3) {
                       } else {
                           viewModel.findUsers()
                       }
                   }
               }
           }

    }

    fun displayUserDetails(id : String){
        viewModel.getUserDetails(id,::showUserDetails)
    }

    fun showUserDetails(details : UserDetails){
         Log.d("details",details.fullName)
        val dialog = AlertDialog.Builder(activity)
        val view = activity?.layoutInflater?.inflate(R.layout.user_profile_dialog,null)
        dialog.setView(view)
        view?.findViewById<TextView>(R.id.user_details_full_name)?.text = details.firstName + " "+details.lastName
        view?.findViewById<TextView>(R.id.user_details_email)?.text = "Email: "+ details.email
        view?.findViewById<TextView>(R.id.user_details_joined_date)?.text = "Joined: "+ details.dateJoined
        view?.findViewById<TextView>(R.id.user_details_friends_num)?.text = "Friends: "+ details.numOfFriends
        val button = view?.findViewById<Button>(R.id.user_details_send_friend_request_button)
        if(details.friendshipRequestReceived){
            button?.text = "Accept friend request"
        }else if(details.friendshipRequestSent){
            button?.isEnabled = false
            button?.text = "Friend request already sent!"
        }else{
            button?.text = "Send friend request"
        }
        view?.findViewById<Button>(R.id.user_details_send_friend_request_button)?.setOnClickListener {
            val callback : () -> Unit = {
                 -> it.isEnabled = false
                if(details.friendshipRequestReceived){
                    (it as Button).text = "You are now friends!"
                }else{
                    (it as Button).text = "Friend request sent!"
                }

            }
            if(details.friendshipRequestReceived){
                viewModel.sendFriendRequest(details.id, callback,true)
            }else{
                viewModel.sendFriendRequest(details.id, callback,true)
            }

        }
        dialog.create().show()
    }


}
