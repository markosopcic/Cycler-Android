package com.markosopcic.cycler.view

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
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.get
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
            var response = api.login(
                LoginForm(
                    email,
                    password,
                    offset
                )
            ).execute()
            if (response.code() != 200) {
                runOnUiThread { Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show(); }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Successful login!", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }.start()
    }
}
