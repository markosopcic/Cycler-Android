package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.EventInvitationResponse
import com.markosopcic.cycler.network.models.FriendRequestResponse
import com.markosopcic.cycler.view.adapters.EventInvitationsAdapter
import com.markosopcic.cycler.view.adapters.FriendRequestsAdapter
import com.markosopcic.cycler.viewmodel.EventInvitationsViewModel
import com.markosopcic.cycler.viewmodel.FriendRequestsViewModel
import kotlinx.android.synthetic.main.friend_requests_fragment.*
import org.koin.android.ext.android.get
import java.util.*

class EventInvitationsFragment : Fragment() {


    private  var viewModel = get<EventInvitationsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(this@EventInvitationsFragment.activity)
        manager.stackFromEnd = false
        invitations_recycler_view.layoutManager = manager
        val adapter = EventInvitationsAdapter(ArrayList<EventInvitationResponse>())
        invitations_recycler_view.adapter = adapter
        viewModel.invitationsRecyclerView.value = invitations_recycler_view
        viewModel.refreshEventInvitations()
        adapter.onAcceptInvitation = viewModel::acceptRequest


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val res = inflater.inflate(R.layout.event_invitations_fragment, container, false)

        return res
    }

}
