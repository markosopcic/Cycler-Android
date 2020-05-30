package com.markosopcic.cycler.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.get

class RegisterActivity : AppCompatActivity() {

    val viewModel = get<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_logo.typeface = Typeface.createFromAsset(assets, "fonts/Lobster-Regular.ttf")
        registerButton.setOnClickListener {
            viewModel.register(::onsuccess)
        }
        firstName.doOnTextChanged { text, start, count, after ->
            viewModel.firstName.value = text.toString()
        }
        lastName.doOnTextChanged { text, start, count, after ->
            viewModel.lastName.value = text.toString()
        }
        email.doOnTextChanged { text, start, count, after ->
            viewModel.email.value = text.toString()
        }
        password.doOnTextChanged { text, start, count, after ->
            viewModel.password.value = text.toString()
        }
        confirmPassword.doOnTextChanged { text, start, count, after ->
            viewModel.confirmPassword.value = text.toString()
        }
    }

    fun onsuccess() {
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }


}
