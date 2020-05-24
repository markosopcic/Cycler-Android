package com.markosopcic.cycler.view

import android.content.Context
import android.content.Intent
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
import com.markosopcic.cycler.network.models.LoginResponse
import com.markosopcic.cycler.utility.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.koin.android.ext.android.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val api = get<CyclerAPI>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        logo.typeface = Typeface.createFromAsset(assets, "fonts/Lobster-Regular.ttf")
        loginButton.setOnClickListener {
            if(emailInput.text.isEmpty() || passwordInput.text.isEmpty()){
                Toast.makeText(this,"Email or password is empty!",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            sendLogin(
                emailInput.text.toString(),
                passwordInput.text.toString()
            )
        }

        val register = findViewById<Button>(R.id.registerButton)
        register.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }


    fun sendLogin(email: String, password: String) {
        Thread {
            val tz = TimeZone.getDefault()
            val now = Date()
            val offset = -tz.getOffset(now.time) / 60000
            api.login(
                LoginForm(
                    email,
                    password,
                    offset
                )
            ).enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    runOnUiThread { Toast.makeText(this@LoginActivity, "Invalid login", Toast.LENGTH_SHORT).show(); }
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful) {
                        getSharedPreferences(Constants.USER_PREFERENCE_KEY, Context.MODE_PRIVATE).edit().putString(Constants.USER_ID_KEY,response.body()?.userId).apply()
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Successful login!",
                                Toast.LENGTH_SHORT
                            ).show()
                            var intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }else{
                        runOnUiThread { Toast.makeText(this@LoginActivity, "Invalid login", Toast.LENGTH_SHORT).show(); }
                    }
                }
            })

        }.start()
    }
}
