package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.FriendRequestResponse
import com.markosopcic.cycler.view.adapters.FriendRequestsAdapter
import kotlinx.android.synthetic.main.invitations_fragment.*
import org.koin.core.context.KoinContextHandler.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FriendRequestsViewModel(private val cyclerAPI :CyclerAPI,val app: Application) : AndroidViewModel(app){

    var invitationsRecyclerView = MutableLiveData<RecyclerView>()


    fun refreshFriendRequests(){
        cyclerAPI.getFriendRequests().enqueue(object :
            Callback<List<FriendRequestResponse>> {
            override fun onFailure(call: Call<List<FriendRequestResponse>>, t: Throwable) {
                Toast.makeText(app,"Couldn't load requests!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<FriendRequestResponse>>,
                response: Response<List<FriendRequestResponse>>
            ) {
                val list = ArrayList<FriendRequestResponse>()
                response.body()?.let { list.addAll(it) }
                (invitationsRecyclerView.value?.adapter as FriendRequestsAdapter?)?.changeDataset(list)
                invitationsRecyclerView.value?.adapter?.notifyDataSetChanged()
                (invitationsRecyclerView.value as RecyclerView?)?.setHasFixedSize(false)
            }

        })
    }
}