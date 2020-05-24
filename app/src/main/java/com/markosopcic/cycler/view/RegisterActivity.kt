package com.markosopcic.cycler.view

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.forms.RegisterForm
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val cyclerAPI : CyclerAPI = get()

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

            val form = RegisterForm(firstName.text.toString(),lastName.text.toString(),password.text.toString(),confirmPassword.text.toString(),email.text.toString())
            cyclerAPI.register(form).enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Something went wrong! Try again",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(!response.isSuccessful){
                        Toast.makeText(this@RegisterActivity, "Something went wrong! Try again",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@RegisterActivity, "Registration successful!",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                        finish()
                    }
                }

            })
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
