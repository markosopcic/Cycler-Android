package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.data.CyclerDatabase
import com.markosopcic.cycler.data.model.Event
import com.markosopcic.cycler.location.LocationService
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.APILocationModel
import com.markosopcic.cycler.network.forms.EventModel
import com.markosopcic.cycler.network.forms.LocationModel
import com.markosopcic.cycler.network.models.EventResponse
import com.markosopcic.cycler.utility.Constants
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

class TrackingViewModel(
    val cyclerAPI: CyclerAPI,
    val cyclerDatabase: CyclerDatabase,
    val app: Application
) : AndroidViewModel(app) {

    var trackingActive = MutableLiveData<Boolean>()
    var onlineTracking = MutableLiveData(false)
    var mBound = MutableLiveData(false)
    var mService: LocationService.LocationBinder? = null
    var eventTracking = MutableLiveData(false)
    var selectedEvent = MutableLiveData<EventResponse>()
    var events: List<EventResponse>? = null
    var trackingStatus = MutableLiveData<TrackingState>()
    var trackedTime = MutableLiveData(Duration.ZERO)
    var timeTrackerDisposable: Disposable? = null
    var lastLocation: Location? = null
    var lastLocationTime: LocalDateTime = LocalDateTime.now()
    var lastUpdatedLocation: LocalDateTime = LocalDateTime.now()
    var distanceMoved = MutableLiveData(0.0)
    var currentEventId: Long? = null


    fun StartTracking(resume: Boolean) {
        StartTimeTracker(resume)
        mService?.service?.locationListener = ::ConsumeLocation
        if (!resume) {
            val remoteId = if (eventTracking.value!!) selectedEvent.value?.id else null
            val ownerId = if (eventTracking.value!!) selectedEvent.value?.ownerId else getUserId()
            currentEventId = cyclerDatabase.eventDao().addEvent(
                Event(
                    null, remoteId, ownerId,
                    System.currentTimeMillis(), "Ride", 0, 0
                )
            )


        }
    }



    fun StopTracking() {
        val dbevent = cyclerDatabase.eventDao().getEventById(currentEventId!!)
        dbevent.Duration = trackedTime.value?.seconds!!
        dbevent.Meters = distanceMoved.value?.toLong()!!
        cyclerDatabase.eventDao().updateEvent(dbevent)
        trackingStatus.value = TrackingState.STOPPED
        trackingActive.value = false
        val dbEvent = cyclerDatabase.eventDao().getEventById(currentEventId!!)
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
            trackedTime.value?.seconds!!,
            distanceMoved.value?.toLong()!!
        )
        StopTimeTracker(false)
        cyclerAPI.uploadEventLocations(event).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    app,
                    "Something went wrong! Try again from the profiles menu!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        app,
                        "Something went wrong! Try again from the profiles menu!",
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


    fun getUserId(): String? {
        return app.getSharedPreferences(Constants.USER_PREFERENCE_KEY, Context.MODE_PRIVATE)
            .getString(Constants.USER_ID_KEY, null)
    }


    fun addLocationToDatabase(location: Location) {
        val loc = com.markosopcic.cycler.data.model.Location(
            null,
            currentEventId!!,
            getUserId()!!,
            location.longitude,
            location.latitude,
            location.time
        )
        cyclerDatabase.locationDao().AddLocation(loc)

    }

    var _eQuatorialEarthRadius = 6378.1370
    var _d2r = Math.PI / 180.0

    private fun HaversineInM(
        lat1: Double,
        long1: Double,
        lat2: Double,
        long2: Double
    ) : Double {
        return (1000.0 * HaversineInKM(lat1, long1, lat2, long2))
    }

    private fun HaversineInKM(
        lat1: Double,
        long1: Double,
        lat2: Double,
        long2: Double
    ): Double {
        val dlong = (long2 - long1) * _d2r
        val dlat = (lat2 - lat1) * _d2r
        val a: Double = Math.pow(
            Math.sin(dlat / 2.0),
            2.0
        ) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r) * Math.pow(
            Math.sin(dlong / 2.0),
            2.0
        )
        val c: Double =
            2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a))
        return _eQuatorialEarthRadius * c
    }



    fun ConsumeLocation(location: Location) {
        Log.d("NextLocation",location.time.toString())
        if (lastLocation != null) {


            val d = HaversineInM(location.latitude,location.longitude,lastLocation!!.latitude,lastLocation!!.longitude)

            if(d ==  0.0){
                lastLocation = location
                return
            }
            addLocationToDatabase(location)

            if (distanceMoved.value == null)
                distanceMoved.value = d
            else {
                distanceMoved.value = distanceMoved.value!! + d
            }
        }
        lastLocation = location

        val updateOnlineStatus = ChronoUnit.SECONDS.between(
            lastUpdatedLocation,
            LocalDateTime.now()
        ) > 10
        if (updateOnlineStatus) {
            lastUpdatedLocation = LocalDateTime.now()
        }

        if (onlineTracking.value!!) {
            val eventId = if (eventTracking.value!!) selectedEvent.value?.id else null
            if (ChronoUnit.SECONDS.between(lastLocationTime,LocalDateTime.now())  < 1){
                return
            } else {
                lastLocationTime = LocalDateTime.now()
            }
            cyclerAPI.sendLocation(
                LocationModel(
                    eventId,
                    location.longitude,
                    location.latitude,
                    updateOnlineStatus
                )
            ).enqueue(object :
                Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                }
            })
        }
    }

    fun StartTimeTracker(resume: Boolean) {
        lastLocation = null
        if (!resume) {
            trackedTime.value = Duration.ZERO
            distanceMoved.value = 0.0
        }
        timeTrackerDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                trackedTime.value = trackedTime.value!!.plusSeconds(1)
            }
    }

    fun StopTimeTracker(pause: Boolean) {
        timeTrackerDisposable?.dispose()
        if (!pause) {
            trackedTime.value = Duration.ZERO
            distanceMoved.value = 0.0
        }
        lastLocation = null

    }


    fun refreshActiveEvents(callback: (List<EventResponse>) -> Unit) {
        cyclerAPI.getActiveEvents().enqueue(object : Callback<List<EventResponse>> {
            override fun onFailure(call: Call<List<EventResponse>>, t: Throwable) {
                Toast.makeText(
                    getApplication(),
                    "Something went wrong! Try again!",
                    Toast.LENGTH_SHORT
                ).show()
                eventTracking.value = false
            }

            override fun onResponse(
                call: Call<List<EventResponse>>,
                response: Response<List<EventResponse>>
            ) {
                if (!response.isSuccessful) {
                    eventTracking.value = false
                    Toast.makeText(
                        getApplication(),
                        "Something went wrong! Try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    events = response.body()
                    callback.invoke(events!!)

                }
            }

        })


    }

    enum class TrackingState {
        STARTED,
        PAUSED,
        STOPPED,
        RESUMED
    }
}