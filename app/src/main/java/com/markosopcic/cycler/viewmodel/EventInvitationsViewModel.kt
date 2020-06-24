package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.EventInvitationResponse
import com.markosopcic.cycler.view.adapters.EventInvitationsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EventInvitationsViewModel(private val cyclerAPI: CyclerAPI, val app: Application) :
    AndroidViewModel(app) {

    val  eventInvitations = MutableLiveData<MutableList<EventInvitationResponse>>(mutableListOf())


    fun acceptRequest(invitationId: String, accept: Boolean, position: Int, callback : ((Int) ->Unit)) {
        cyclerAPI.acceptInvitation(invitationId, accept).enqueue(object : Callback<Void> {
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

    fun refreshEventInvitations(callback : ((Boolean) -> Unit)?) {
        cyclerAPI.getEventInvitations().enqueue(object :
            Callback<List<EventInvitationResponse>> {
            override fun onFailure(call: Call<List<EventInvitationResponse>>, t: Throwable) {
                Toast.makeText(app, "Couldn't load requests!", Toast.LENGTH_SHORT).show()
                callback?.invoke(false)
            }

            override fun onResponse(
                call: Call<List<EventInvitationResponse>>,
                response: Response<List<EventInvitationResponse>>
            ) {
                if(response.isSuccessful){
                    val list = ArrayList<EventInvitationResponse>()
                    response.body()?.let { list.addAll(it) }
                    eventInvitations.value = list
                    callback?.invoke(true)
                }else{
                    callback?.invoke(false)
                }

            }

        })
    }
}