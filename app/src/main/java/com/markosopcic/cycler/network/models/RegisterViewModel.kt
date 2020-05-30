package com.markosopcic.cycler.network.models

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.RegisterForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(val cyclerAPI: CyclerAPI, val app: Application) : AndroidViewModel(app) {

    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")
    val email = MutableLiveData("")

    fun register(callback: (() -> Unit)) {
        if (password.value != null && password.value != confirmPassword.value) {
            Toast.makeText(app, "Passwords do not match!", Toast.LENGTH_LONG).show()
        } else if (!isEmailValid(email.value)) {
            Toast.makeText(app, "Invalid email!", Toast.LENGTH_LONG).show()
        } else if (password.value!!.length < 6) {
            Toast.makeText(app, "Password too short!", Toast.LENGTH_LONG).show()
        } else if (firstName.value!!.isEmpty() || lastName.value!!.isEmpty()) {
            Toast.makeText(app, "Please set your name!", Toast.LENGTH_LONG).show()
        }

        val form = RegisterForm(
            firstName.value!!,
            lastName.value!!,
            password.value!!,
            confirmPassword.value!!,
            email.value!!
        )
        cyclerAPI.register(form).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(app, "Something went wrong! Try again", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        app, "Something went wrong! Try again",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    callback.invoke()
                }
            }

        })
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}