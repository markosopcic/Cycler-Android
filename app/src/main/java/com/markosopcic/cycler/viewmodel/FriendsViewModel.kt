package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.UserDetails
import com.markosopcic.cycler.network.models.UserSearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsViewModel(val cyclerAPI: CyclerAPI, application: Application) :
    AndroidViewModel(application) {

    val searchInput = MutableLiveData<String>()
    val currentUsers = MutableLiveData<List<UserSearchResult>>()

    fun findUsers() {
        if (searchInput.value?.length!! < 3)
            return
        cyclerAPI.searchUsers(searchInput.value!!)
            .enqueue(object : Callback<List<UserSearchResult>> {
                override fun onFailure(call: Call<List<UserSearchResult>>, t: Throwable) {
                    Toast.makeText(
                        getApplication(),
                        "Something went wrong while loading users!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<List<UserSearchResult>>,
                    response: Response<List<UserSearchResult>>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            getApplication(),
                            "Something went wrong while loading users!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (response.body()?.size == 0) {
                            Toast.makeText(getApplication(), "No users found!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        currentUsers.value = response.body()
                    }
                }

            })
    }


    fun getUserDetails(id: String, callback: (UserDetails) -> Unit) {
        cyclerAPI.getUserProfile(id).enqueue(object : Callback<UserDetails> {
            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                Toast.makeText(
                    getApplication(),
                    "Something went wrong, try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        getApplication(),
                        "Something went wrong, try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    callback.invoke(response.body()!!)
                }
            }

        })
    }

    fun sendFriendRequest(id: String, callback: () -> Unit, accept: Boolean) {
        if (!accept) {
            cyclerAPI.sendFriendRequest(id).enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        getApplication(),
                        "Something went wrong, try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            getApplication(),
                            "Something went wrong, try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        callback.invoke()
                    }
                }

            })
        } else {
            cyclerAPI.acceptFriendRequest(id, true).enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        getApplication(),
                        "Something went wrong, try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            getApplication(),
                            "Something went wrong, try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        callback.invoke()
                    }
                }

            })
        }

    }


}