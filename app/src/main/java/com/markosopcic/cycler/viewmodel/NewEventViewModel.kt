package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.CreateEventForm
import com.markosopcic.cycler.network.models.FriendResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZoneOffset
import java.time.temporal.TemporalAccessor
import java.util.*

class NewEventViewModel(val cyclerAPI: CyclerAPI,val app : Application) : AndroidViewModel(app){

    val eventName = MutableLiveData("")
    val eventDescription = MutableLiveData("")
    val selectedFriends = MutableLiveData<List<String>>()
    val startTime = MutableLiveData<Calendar>()


    fun createEvent(callback: (() -> Unit)){
        if(eventName.value == null || eventName.value!!.length < 3){
            Toast.makeText(app,"Please set a longer name!",Toast.LENGTH_SHORT).show()
            return
        }else if(eventDescription.value == null){
            Toast.makeText(app,"Please set a longer description!",Toast.LENGTH_SHORT).show()
            return
        }else if(selectedFriends.value == null || selectedFriends.value!!.isEmpty()){
            Toast.makeText(app,"Please invite some friends!",Toast.LENGTH_SHORT).show()
            return
        }else if(startTime.value == null){
            Toast.makeText(app,"Please set a time!",Toast.LENGTH_SHORT).show()
            return
        }

        val form = CreateEventForm(eventName.value!!,eventDescription.value!!,startTime.value!!.toInstant().toEpochMilli(), selectedFriends.value!!)
        cyclerAPI.createEvent(form).enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(app,"Something went wrong!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
               if(!response.isSuccessful){
                   Toast.makeText(app,"Something went wrong!",Toast.LENGTH_SHORT).show()
               }else{
                   eventName.value = null
                   eventDescription.value = null
                   selectedFriends.value = mutableListOf()
                   startTime.value = null
                   Toast.makeText(app,"Event created successfully!",Toast.LENGTH_SHORT).show()
                   callback.invoke()
               }
            }

        })
    }


    fun loadFriends(callback : ((List<FriendResult>) -> Unit)){
        cyclerAPI.getFriends().enqueue(object : Callback<List<FriendResult>>{
            override fun onFailure(call: Call<List<FriendResult>>, t: Throwable) {
                Toast.makeText(app,"Something went wrong!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<FriendResult>>,
                response: Response<List<FriendResult>>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(app,"Something went wrong!",Toast.LENGTH_SHORT).show()
                }else{
                    callback.invoke(response.body()!!)
                }
            }

        })
    }

}