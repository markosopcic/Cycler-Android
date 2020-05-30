package com.markosopcic.cycler.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.LoginForm
import com.markosopcic.cycler.network.models.LoginResponse
import com.markosopcic.cycler.utility.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginViewModel(val api: CyclerAPI, val app: Application) : AndroidViewModel(app) {

    var emailInput = MutableLiveData<String>()
    var passwordInput = MutableLiveData<String>()

    fun sendLogin(callback: (() -> Unit)) {
        val tz = TimeZone.getDefault()
        val now = Date()
        val offset = -tz.getOffset(now.time) / 60000
        api.login(
            LoginForm(
                emailInput.value!!,
                passwordInput.value!!,
                offset
            )
        ).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(app, "Invalid login", Toast.LENGTH_SHORT).show();
            }

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    app.getSharedPreferences(Constants.USER_PREFERENCE_KEY, Context.MODE_PRIVATE)
                        .edit().putString(
                        Constants.USER_ID_KEY, response.body()?.userId
                    ).apply()
                    Toast.makeText(
                        app,
                        "Successful login!",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback.invoke()

                } else {
                    Toast.makeText(app, "Invalid login", Toast.LENGTH_SHORT).show();
                }
            }
        })

    }
}