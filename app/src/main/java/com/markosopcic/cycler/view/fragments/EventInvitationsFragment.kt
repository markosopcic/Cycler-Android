package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.EventInvitationResponse
import com.markosopcic.cycler.view.adapters.EventInvitationsAdapter
import com.markosopcic.cycler.viewmodel.EventInvitationsViewModel
import kotlinx.android.synthetic.main.event_invitations_fragment.*
import kotlinx.android.synthetic.main.friend_requests_fragment.*
import kotlinx.android.synthetic.main.friend_requests_fragment.invitations_recycler_view
import org.koin.android.ext.android.get
import java.util.*

class EventInvitationsFragment : Fragment() {


    private var viewModel = get<EventInvitationsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(this@EventInvitationsFragment.activity)
        manager.stackFromEnd = false
        invitations_recycler_view.layoutManager = manager
        val adapter = EventInvitationsAdapter(ArrayList<EventInvitationResponse>())
        invitations_recycler_view.adapter = adapter
        viewModel.refreshEventInvitations(null)
        adapter.onAcceptInvitation = viewModel::acceptRequest

        event_invitations_refresh.setOnRefreshListener {
            viewModel.refreshEventInvitations{
                event_invitations_refresh.isRefreshing = false
                if(!it){
                    Toast.makeText(requireActivity(),"Error while loading invitations!",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.eventInvitations.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (adapter as EventInvitationsAdapter?)?.changeDataset(
                it
            )
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.event_invitations_fragment, container, false)
    }



}
