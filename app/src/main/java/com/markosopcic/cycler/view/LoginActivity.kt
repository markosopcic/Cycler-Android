package com.markosopcic.cycler.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.markosopcic.cycler.R
import com.markosopcic.cycler.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.get

class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        logo.typeface = Typeface.createFromAsset(assets, "fonts/Lobster-Regular.ttf")
        loginButton.setOnClickListener {
            if (emailInput.text.isEmpty() || passwordInput.text.isEmpty()) {
                Toast.makeText(this, "Email or password is empty!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.sendLogin(
                ::loginCallback
            )
        }

        emailInput.doOnTextChanged { text, start, count, after ->
            viewModel.emailInput.value = text.toString()
        }
        passwordInput.doOnTextChanged { text, start, count, after ->
            viewModel.passwordInput.value = text.toString()
        }

        val register = findViewById<Button>(R.id.registerButton)
        register.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    fun loginCallback() {
        var intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
