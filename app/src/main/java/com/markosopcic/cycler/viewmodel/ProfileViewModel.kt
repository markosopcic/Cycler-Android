package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.UserProfileResponse
import com.markosopcic.cycler.utility.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(val cyclerAPI: CyclerAPI, val app : Application) : AndroidViewModel(app){

    val userData  = MutableLiveData<UserProfileResponse>()


    fun logout(callback : (() -> Unit)){
        app.getSharedPreferences(Constants.USER_PREFERENCE_KEY, Context.MODE_PRIVATE).edit().remove(Constants.USER_ID_KEY).apply()
        app.getSharedPreferences(Constants.COOKIES_PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putStringSet(Constants.COOKIES_PREFERENCE_KEY,HashSet<String>()).apply()
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