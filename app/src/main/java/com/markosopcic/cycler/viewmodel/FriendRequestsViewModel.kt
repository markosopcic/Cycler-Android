package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.FriendRequestResponse
import com.markosopcic.cycler.view.adapters.FriendRequestsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FriendRequestsViewModel(private val cyclerAPI: CyclerAPI, val app: Application) :
    AndroidViewModel(app) {

    var invitations = MutableLiveData<MutableList<FriendRequestResponse>>(mutableListOf())

    fun acceptRequest(requesterId: String, accept: Boolean, position: Int,callback : ((Int) -> Unit)) {
        cyclerAPI.acceptFriendRequest(requesterId, accept).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(app, "Error while accepting request!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(!response.isSuccessful){
                    Toast.makeText(app, "Error while accepting request!", Toast.LENGTH_SHORT).show()
                }else{
                    callback.invoke(position)
                }

            }

        })

    }

    fun refreshFriendRequests() {
        cyclerAPI.getFriendRequests().enqueue(object :
            Callback<List<FriendRequestResponse>> {
            override fun onFailure(call: Call<List<FriendRequestResponse>>, t: Throwable) {
                Toast.makeText(app, "Couldn't load requests!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<FriendRequestResponse>>,
                response: Response<List<FriendRequestResponse>>
            ) {
                val list = ArrayList<FriendRequestResponse>()
                response.body()?.let { list.addAll(it) }
                invitations.value = list
            }

        })
    }
}