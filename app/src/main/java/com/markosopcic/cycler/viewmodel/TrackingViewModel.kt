package com.markosopcic.cycler.viewmodel

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.EventResponse
import com.markosopcic.cycler.view.adapters.ActiveEventsAdapter
import org.koin.android.ext.android.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackingViewModel(val cyclerAPI: CyclerAPI, application: Application) : AndroidViewModel(application){

    var trackingActive = MutableLiveData<Boolean>()
    var onlineTracking = MutableLiveData<Boolean>()
    var eventTracking =  MutableLiveData<Boolean>()
    var selectedEvent = MutableLiveData<EventResponse>()
    var events : List<EventResponse>? = null
    var trackingStatus = MutableLiveData<TrackingState>()


    fun refreshActiveEvents(activity : Activity){
        cyclerAPI.getActiveEvents().enqueue(object : Callback<List<EventResponse>>
            {
            override fun onFailure(call: Call<List<EventResponse>>, t: Throwable) {
                Toast.makeText(getApplication(),"Something went wrong! Try again!",Toast.LENGTH_SHORT).show()
                eventTracking.value = false
            }

            override fun onResponse(
                call: Call<List<EventResponse>>,
                response: Response<List<EventResponse>>
            ) {
                if(!response.isSuccessful){
                    eventTracking.value = false
                    Toast.makeText(getApplication(),"Something went wrong! Try again!",Toast.LENGTH_SHORT).show()
                }else{
                    events = response.body()
                    val adapter = ActiveEventsAdapter(activity,events!!)
                    adapter.updateData(events!!)
                    val dialog = AlertDialog.Builder(activity).setAdapter(adapter) { _, which -> selectedEvent.value = events!![which] }.setTitle("Select event").setNegativeButton("Cancel"
                    ) { _, _ -> eventTracking.value = false}.create()
                    val listView = dialog.listView
                    listView.divider =  ColorDrawable(Color.WHITE)
                    listView.dividerHeight = 2
                    dialog.show()
                }
            }

        })



    }

    enum class TrackingState{
        STARTED,
        PAUSED,
        STOPPED,
        RESUMED
    }
}