package com.markosopcic.cycler.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.MainActivity


class LocationService : Service() {


    private val mBinder: IBinder = LocationBinder()

    var locationManager: LocationManager? = null
    var locationListener: ((Location) -> Unit)? = null
    private var primaryListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationListener?.invoke(location)

        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        locationManager?.removeUpdates(primaryListener)
        locationListener = null
        stopForeground(true)

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == "stop") {
            onDestroy()
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setContentTitle("Position Tracker")
            .setContentText("Tracking rider")
            .setSmallIcon(R.drawable.ic_person_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Please give location permission!", Toast.LENGTH_SHORT).show()
            return super.onStartCommand(intent, flags, startId)
        }
        startForeground(1, notification)
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            primaryListener
        )
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    inner class LocationBinder : Binder() {
        val service: LocationService
            get() = this@LocationService
    }


    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }
}