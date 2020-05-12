package com.markosopcic.cycler.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.utility.Constants
import org.koin.android.ext.android.get
import java.util.*

class MainActivity : AppCompatActivity() {

    var cyclerAPI = get<CyclerAPI>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isUserLoggedIn()) {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        var bottomNavigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeButton -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.invitationsTab -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.logout -> {
                    logout()
                    true
                }
                R.id.startTracking -> {
                    true
                }
                else -> false
            }
        }
    }

    fun logout(): Unit{
        Thread {
            var response = cyclerAPI.logout().execute()
            if(response.isSuccessful){
                getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE).getStringSet(Constants.PREFERENCE_KEY,null)?.clear()
                var intent = Intent(this, LoginActivity::class.java)
                runOnUiThread{startActivity(intent)}
                finish()
            }
        }.start()

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
