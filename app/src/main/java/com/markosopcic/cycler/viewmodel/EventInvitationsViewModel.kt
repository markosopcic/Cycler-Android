package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.EventInvitationResponse
import com.markosopcic.cycler.network.models.FriendRequestResponse
import com.markosopcic.cycler.view.adapters.EventInvitationsAdapter
import com.markosopcic.cycler.view.adapters.FriendRequestsAdapter
import kotlinx.android.synthetic.main.friend_requests_fragment.*
import kotlinx.coroutines.*
import org.koin.core.context.KoinContextHandler.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.concurrent.Executors

class EventInvitationsViewModel(private val cyclerAPI :CyclerAPI, val app: Application) : AndroidViewModel(app){

    var invitationsRecyclerView = MutableLiveData<RecyclerView>()

     fun acceptRequest( invitationId : String, accept : Boolean,position : Int){
         cyclerAPI.acceptInvitation(invitationId,accept).enqueue(object : Callback<Void>{
             override fun onFailure(call: Call<Void>, t: Throwable) {
                 Toast.makeText(app,"Error while accepting request!",Toast.LENGTH_SHORT).show()
             }
             override fun onResponse(call: Call<Void>, response: Response<Void>) {
                 (invitationsRecyclerView.value?.adapter as EventInvitationsAdapter).removeItem(position)
             }

         })

    }

    fun refreshEventInvitations(){
        cyclerAPI.getEventInvitations().enqueue(object :
            Callback<List<EventInvitationResponse>> {
            override fun onFailure(call: Call<List<EventInvitationResponse>>, t: Throwable) {
                Toast.makeText(app,"Couldn't load requests!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<EventInvitationResponse>>,
                response: Response<List<EventInvitationResponse>>
            ) {
                val list = ArrayList<EventInvitationResponse>()
                response.body()?.let { list.addAll(it) }
                (invitationsRecyclerView.value?.adapter as EventInvitationsAdapter?)?.changeDataset(list)
            }

        })
    }
}