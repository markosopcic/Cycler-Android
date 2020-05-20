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
import android.util.Log
import androidx.core.app.NotificationCompat
import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.MainActivity
import io.reactivex.disposables.Disposable
import java.util.*


class LocationService : Service() {
    var interval = 1000
    private val mBinder: IBinder = LocationBinder()
    var locationListeners: MutableList<ILocationListener> =
        ArrayList()
    var locationManager: LocationManager? = null
    var lastLocation: Location? = null
    var listeners : List<ILocationListener> = listOf()
    private var primaryListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (lastLocation != null) {
                val p = 0.017453292519943295 // Math.PI / 180
                val a =
                    0.5 - Math.cos((location.latitude - lastLocation!!.latitude) * p) / 2 +
                            Math.cos(lastLocation!!.latitude * p) * Math.cos(location.latitude * p) *
                            (1 - Math.cos((location.longitude - lastLocation!!.longitude) * p)) / 2
                val d = 12742 * Math.asin(Math.sqrt(a)) * 1000
                Log.d(
                    "Position_debug",
                    String.format("%.2f %f", d, location.accuracy)
                )
            }
            if (lastLocation == null || location.time - lastLocation!!.time > interval || location.accuracy < lastLocation!!.accuracy)
            {
                lastLocation = location
                for(listener in listeners){
                    listener.nextLocation(location)
                }
            }
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

    var task: Disposable? = null
    override fun onDestroy() {
        if (task != null) {
            task!!.dispose()
        }
        locationManager!!.removeUpdates(primaryListener)
        locationListeners.clear()
        stopForeground(true)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == "stop") {
            onDestroy()
            return super.onStartCommand(intent, flags, startId)
        }
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val input = intent.getStringExtra("inputExtra")
        interval = intent.getIntExtra("interval", interval)
        val networkGPS = intent.getBooleanExtra("network", false)
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
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_person_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return super.onStartCommand(intent, flags, startId)
        }
        startForeground(1, notification)

        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            interval.toLong(),
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
            manager.createNotificationChannel(serviceChannel)
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