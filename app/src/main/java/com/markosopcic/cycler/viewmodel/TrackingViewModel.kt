package com.markosopcic.cycler.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI

class TrackingViewModel(cyclerAPI: CyclerAPI,application: Application) : AndroidViewModel(application){

    var trackingActive = MutableLiveData<Boolean>()
    var onlineTracking = MutableLiveData<Boolean>()
}