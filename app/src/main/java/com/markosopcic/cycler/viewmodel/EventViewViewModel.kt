package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.UserEventViewResponse
import com.markosopcic.cycler.utility.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewViewModel(val cyclerAPI: CyclerAPI, val app : Application) : AndroidViewModel(app) {

    var feedItems = MutableLiveData<List<UserEventViewResponse>>(mutableListOf())


    fun loadMoreUserEvents(callback: ((Boolean) -> Unit)?){
        cyclerAPI.getUserEvents(feedItems.value!!.size, Constants.FEED_PAGE_SIZE).enqueue(object :
            Callback<List<UserEventViewResponse>>{
            override fun onFailure(call: Call<List<UserEventViewResponse>>, t: Throwable) {
            Toast.makeText(app,"Something went wrong! Try again later!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<UserEventViewResponse>>,
                response: Response<List<UserEventViewResponse>>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(app,"Something went wrong! Try again later!",Toast.LENGTH_SHORT).show()
                }else{
                    val list = mutableListOf<UserEventViewResponse>()
                    list.addAll(feedItems.value!!)
                    list.addAll(response.body()!!)
                    feedItems.value = list
                    if(response.body()!!.size == 0){
                        callback!!.invoke(false)
                    }else{
                        callback!!.invoke(true)
                    }
                }
            }

        })
    }


}