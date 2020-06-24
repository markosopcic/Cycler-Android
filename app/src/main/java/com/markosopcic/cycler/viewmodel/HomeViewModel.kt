package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.EventFeedResponse
import com.markosopcic.cycler.utility.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(val cyclerAPI: CyclerAPI, val app: Application) : AndroidViewModel(app) {

    var eventFeed = MutableLiveData<MutableList<EventFeedResponse>>(arrayListOf())

    fun refreshFeedItems(callback: ((Boolean) -> Unit)?){
        cyclerAPI.getEventFeed(0,Constants.FEED_PAGE_SIZE).enqueue(object : Callback<List<EventFeedResponse>>{
            override fun onFailure(call: Call<List<EventFeedResponse>>, t: Throwable) {
                callback?.invoke(false)
            }

            override fun onResponse(
                call: Call<List<EventFeedResponse>>,
                response: Response<List<EventFeedResponse>>
            ) {
                if(response.isSuccessful){
                    eventFeed.value = response.body() as MutableList<EventFeedResponse>?
                    callback?.invoke(true)
                }else{
                    callback?.invoke(false)
                }
            }

        })
    }

    fun getMoreEventFeedItems(callback: ((Boolean) -> Unit)?) {
        cyclerAPI.getEventFeed(eventFeed.value!!.size, Constants.FEED_PAGE_SIZE)
            .enqueue(object : Callback<List<EventFeedResponse>> {
                override fun onFailure(call: Call<List<EventFeedResponse>>, t: Throwable) {
                    Toast.makeText(
                        app,
                        "Something went wrong, please try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<List<EventFeedResponse>>,
                    response: Response<List<EventFeedResponse>>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            app,
                            "Something went wrong, please try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (response.body()!!.size > 0) {

                            var list = mutableListOf<EventFeedResponse>()
                            list.addAll(eventFeed.value!!)
                            list.addAll(response.body()!!)
                            eventFeed.value = list

                        } else {
                            callback?.invoke(false)
                        }
                    }
                }

            })
    }
}