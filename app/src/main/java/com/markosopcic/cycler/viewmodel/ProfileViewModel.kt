package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.data.CyclerDatabase
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.APILocationModel
import com.markosopcic.cycler.network.forms.EventModel
import com.markosopcic.cycler.network.models.UserProfileResponse
import com.markosopcic.cycler.utility.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(val cyclerAPI: CyclerAPI,val cyclerDatabase: CyclerDatabase, val app : Application) : AndroidViewModel(app){

    val userData  = MutableLiveData<UserProfileResponse>()


    fun getUserId(): String? {
        return app.getSharedPreferences(Constants.USER_PREFERENCE_KEY, Context.MODE_PRIVATE)
            .getString(Constants.USER_ID_KEY, null)
    }

    fun uploadAllEvents(callback : (() -> Unit)){
        val events = cyclerDatabase.eventDao().getAllEvents()

        if(events.size == 0){
            Toast.makeText(app,"No events to upload!",Toast.LENGTH_SHORT).show()
            return
        }else{
            Toast.makeText(app,"Uploading " + events.size.toString()+ " events!",Toast.LENGTH_SHORT).show()
        }
        for(dbEvent in events){
            val locations = cyclerDatabase.locationDao().GetLocationsForEvent(dbEvent.localId!!)
            val remoteLocations = mutableListOf<APILocationModel>()

            for (loc in locations) {
                remoteLocations.add(APILocationModel(loc.longitude, loc.latitude, loc.time))
            }
            val event = EventModel(
                getUserId()!!,
                dbEvent.remoteId,
                dbEvent.startTime,
                System.currentTimeMillis(),
                dbEvent.name,
                dbEvent.ownerId,
                remoteLocations,
                dbEvent.Duration,
                dbEvent.Meters
            )

            cyclerAPI.uploadEventLocations(event).enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        app,
                        "Something went wrong! Try again later!",
                        Toast.LENGTH_SHORT
                    ).show()
                    if(dbEvent == events[events.size - 1]){
                        callback.invoke()
                    }
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(dbEvent == events[events.size - 1]){
                        callback.invoke()
                    }
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            app,
                            "Something went wrong! Try again later!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(app, "Event uploaded successfully!", Toast.LENGTH_SHORT).show()
                        cyclerDatabase.locationDao().RemoveLocations(locations)
                        cyclerDatabase.eventDao().deleteEvent(dbEvent)
                    }

                }

            })
        }



    }

    fun logout(callback : (() -> Unit)){
        app.getSharedPreferences(Constants.USER_PREFERENCE_KEY, Context.MODE_PRIVATE).edit().remove(Constants.USER_ID_KEY).apply()
        app.getSharedPreferences(Constants.COOKIES_PREFERENCE_NAME, Context.MODE_PRIVATE).edit().remove(Constants.COOKIES_PREFERENCE_KEY).apply()
        cyclerAPI.logout().enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.invoke()
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                callback.invoke()
            }

        })
    }

    fun refreshUserData(){
        cyclerAPI.getUserProfile().enqueue(object : Callback<UserProfileResponse> {
            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(app,"Something went wrong!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if(!response.isSuccessful){
                    Toast.makeText(app,"Something went wrong!",Toast.LENGTH_SHORT).show()
                }else{
                    userData.value = response.body()
                }
            }

        })
    }
}