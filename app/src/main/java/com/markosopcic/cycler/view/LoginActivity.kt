package com.markosopcic.cycler.view

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.LoginForm
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {

    private val api = get<CyclerAPI>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val logo = findViewById<TextView>(R.id.logo)
        val login = findViewById<Button>(R.id.loginButton)
        val emailView = findViewById<TextView>(R.id.emailInput)
        val passwordView = findViewById<TextView>(R.id.passwordInput)
        logo.typeface = Typeface.createFromAsset(assets, "fonts/Lobster-Regular.ttf")
        login.setOnClickListener {
            sendLogin(
                emailView.text.toString(),
                passwordView.text.toString()
            )
        }

        val register = findViewById<Button>(R.id.registerButton)
        register.setOnClickListener {
            Thread {

                var response = api.searchUsers("dar").execute()
                for (us in response.body()!!) {
                    Log.d("NETWOKRING", us.fullName)
                }
            }.start()
        }

    }


    fun sendLogin(email: String, password: String) {
        Thread {
            var offset = -120
            var response = api.login(
                LoginForm(
                    email,
                    password
                )
            ).execute()
            if (response.code() != 200) {
                runOnUiThread { Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show(); }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Successful login!", Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }
}
