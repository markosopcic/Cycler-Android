package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.FriendRequestResponse
import com.markosopcic.cycler.view.adapters.FriendRequestsAdapter
import com.markosopcic.cycler.viewmodel.FriendRequestsViewModel
import kotlinx.android.synthetic.main.invitations_fragment.*
import org.koin.android.ext.android.get
import java.util.*

class InvitationsFragment : Fragment() {


    private var cyclerAPI = get<CyclerAPI>()
    private  var viewModel = get<FriendRequestsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(this@InvitationsFragment.activity)
        manager.stackFromEnd = false
        invitations_recycler_view.layoutManager = manager
        invitations_recycler_view.adapter = FriendRequestsAdapter(ArrayList<FriendRequestResponse>())
        viewModel.invitationsRecyclerView.value = invitations_recycler_view
        viewModel.refreshFriendRequests()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val res = inflater.inflate(R.layout.invitations_fragment, container, false)

        return res
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
