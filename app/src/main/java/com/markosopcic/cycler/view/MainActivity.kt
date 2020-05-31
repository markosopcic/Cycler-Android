package com.markosopcic.cycler.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.utility.Constants
import com.markosopcic.cycler.view.fragments.*
import org.koin.android.ext.android.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var invitationsFragment: SocialFragment
    lateinit var trackingFragment: TrackingFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var eventsFragment : EventsFragment
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
            when (item.itemId) {
                R.id.homeButton -> {
                    if (!::homeFragment.isInitialized) {
                        homeFragment = HomeFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.placeholder, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                    true
                }
                R.id.invitationsTab -> {
                    if (!::invitationsFragment.isInitialized) {
                        invitationsFragment = SocialFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.placeholder, invitationsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                    true
                }
                R.id.profile -> {
                    if (!::profileFragment.isInitialized) {
                        profileFragment = ProfileFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.placeholder, profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()

                    true
                }
                R.id.startTracking -> {
                    if (!::trackingFragment.isInitialized) {
                        trackingFragment = TrackingFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.placeholder, trackingFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                    true
                }

                R.id.eventsTab -> {
                    if(!::eventsFragment.isInitialized){
                        eventsFragment = EventsFragment()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.placeholder, eventsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                    true
                }
                else -> false
            }


        }

        if (!::homeFragment.isInitialized) {
            homeFragment = HomeFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.placeholder, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)


    }



    fun isUserLoggedIn(): Boolean {
        val cookie =
            getSharedPreferences(Constants.COOKIES_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(Constants.COOKIES_PREFERENCE_KEY,null)

        return cookie != null && cookie.isNotEmpty()

    }
}
