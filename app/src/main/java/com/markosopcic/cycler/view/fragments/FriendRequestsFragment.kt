package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.FriendRequestResponse
import com.markosopcic.cycler.view.adapters.FriendRequestsAdapter
import com.markosopcic.cycler.viewmodel.FriendRequestsViewModel
import kotlinx.android.synthetic.main.friend_requests_fragment.*
import org.koin.android.ext.android.get
import java.util.*

class FriendRequestsFragment : Fragment() {


    private  var viewModel = get<FriendRequestsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(this@FriendRequestsFragment.activity)
        manager.stackFromEnd = false
        invitations_recycler_view.layoutManager = manager
        val adapter = FriendRequestsAdapter(ArrayList<FriendRequestResponse>())
        invitations_recycler_view.adapter = adapter
        viewModel.invitationsRecyclerView.value = invitations_recycler_view
        viewModel.refreshFriendRequests()
        adapter.onAcceptRequest = viewModel::acceptRequest


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.friend_requests_fragment, container, false)
    }

    fun refreshRequests(){
        viewModel.refreshFriendRequests()
    }

}
