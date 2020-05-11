package com.markosopcic.cycler.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markosopcic.cycler.R
import com.markosopcic.cycler.utility.Constants
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isUserLoggedIn()) {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun isUserLoggedIn(): Boolean {
        val cookies =
            getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE).getStringSet(
                Constants.PREFERENCE_KEY,
                HashSet()
            ) as HashSet<String>?

        return cookies != null && cookies.size > 0

    }
}
