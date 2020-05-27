package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.location.ILocationListener
import com.markosopcic.cycler.location.LocationService
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.LocationModel
import com.markosopcic.cycler.network.models.EventResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class TrackingViewModel(val cyclerAPI: CyclerAPI, application: Application) : AndroidViewModel(application){

    var trackingActive = MutableLiveData<Boolean>()
    var onlineTracking = MutableLiveData<Boolean>(false)
    var mBound  = MutableLiveData<Boolean>(false)
    var mService : LocationService.LocationBinder? = null
    var eventTracking =  MutableLiveData<Boolean>(false)
    var selectedEvent = MutableLiveData<EventResponse>()
    var events : List<EventResponse>? = null
    var trackingStatus = MutableLiveData<TrackingState>()
    var trackedTime = MutableLiveData(Duration.ZERO)
    var timeTrackerDisposable : Disposable? = null
    var lastLocation : Location? = null
    var lastUpdatedLocation : LocalDateTime = LocalDateTime.now()
    var distanceMoved = MutableLiveData<Double>(0.0)

    fun StartTracking(resume : Boolean){
        StartTimeTracker(resume)
        mService?.service?.locationListener = ::ConsumeLocation
    }

    fun ConsumeLocation(location : Location){
        if (lastLocation != null) {
            val p = 0.017453292519943295
            val a =
                0.5 - Math.cos((location.latitude - lastLocation!!.latitude) * p) / 2 +
                        Math.cos(lastLocation!!.latitude * p) * Math.cos(location.latitude * p) *
                        (1 - Math.cos((location.longitude - lastLocation!!.longitude) * p)) / 2
            val d = 12742 * Math.asin(Math.sqrt(a)) * 1000
            Log.d(
                "Position_debug",
                String.format("%.2f %f", d, location.accuracy)
            )
            if(distanceMoved.value == null)
                distanceMoved.value = d
            else{
                distanceMoved.value = distanceMoved.value!! + d
            }

        }
        if (lastLocation == null  || location.accuracy < lastLocation!!.accuracy || location.time > lastLocation!!.time)
        {
            lastLocation = location
            val updateOnlineStatus = ChronoUnit.SECONDS.between(lastUpdatedLocation,
                LocalDateTime.now()) > 10
            if(updateOnlineStatus){
                lastUpdatedLocation = LocalDateTime.now()
            }
            val eventId = if(eventTracking.value!!)  selectedEvent.value?.id else null
            cyclerAPI.sendLocation(LocationModel(eventId,location.longitude,location.latitude,updateOnlineStatus)).enqueue(object :
                Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                }
            })
        }
    }

    fun StartTimeTracker(resume : Boolean){
        lastLocation = null
        if(!resume){
            trackedTime.value = Duration.ZERO
            distanceMoved.value = 0.0
        }
        timeTrackerDisposable = Observable.interval(0,1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                trackedTime.value = trackedTime.value!!.plusSeconds(1)
        }
        }

    fun StopTimeTracker(pause : Boolean){
        timeTrackerDisposable?.dispose()
        if(!pause){
            trackedTime.value = Duration.ZERO
            distanceMoved.value = 0.0
        }
        lastLocation = null

    }


    fun refreshActiveEvents(callback : (List<EventResponse>) -> Unit ){
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
                    callback.invoke(events!!)

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