package com.markosopcic.cycler.view

import android.graphics.Typeface
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.markosopcic.cycler.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_logo.typeface = Typeface.createFromAsset(assets, "fonts/Lobster-Regular.ttf")
        registerButton.setOnClickListener {
            if(password.text.toString() != confirmPassword.text.toString()){
                Toast.makeText(this,"Passwords do not match!",Toast.LENGTH_LONG).show()
            }else if(!isEmailValid(email.text)){
                Toast.makeText(this,"Invalid email!",Toast.LENGTH_LONG).show()
            }else if(password.text.length < 6){
                Toast.makeText(this,"Password too short!",Toast.LENGTH_LONG).show()
            }else if(firstName.text.isEmpty() || lastName.text.isEmpty()){
                Toast.makeText(this,"Please set your name!",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
