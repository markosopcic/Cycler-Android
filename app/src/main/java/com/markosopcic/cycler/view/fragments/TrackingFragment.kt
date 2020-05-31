package com.markosopcic.cycler.view.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.markosopcic.cycler.R
import com.markosopcic.cycler.location.LocationService
import com.markosopcic.cycler.network.models.EventResponse
import com.markosopcic.cycler.view.adapters.ActiveEventsAdapter
import com.markosopcic.cycler.viewmodel.TrackingViewModel
import kotlinx.android.synthetic.main.tracking_fragment.*
import org.koin.android.ext.android.get
import java.math.BigDecimal
import java.math.RoundingMode


class TrackingFragment : Fragment() {


    val viewModel = get<TrackingViewModel>()
    val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.mBound.value = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            viewModel.mService = service as LocationService.LocationBinder?
            viewModel.mBound.value = true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.tracking_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackEventSwitch.setOnClickListener {
            viewModel.eventTracking.value = (it as Switch).isChecked
            if (it.isChecked) {
                viewModel.refreshActiveEvents(::displaySelectEvent)
            }
        }

        viewModel.mBound.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.mService!!.service.locationListener = ::consumeLocation
            }
        })

        liveTrackingSwitch.setOnClickListener {
            viewModel.onlineTracking.value = (it as Switch).isChecked
        }

        viewModel.trackedTime.observe(viewLifecycleOwner, Observer {
            trackingTimeTracker.text = it.toMinutes().toString()
                .padStart(2, '0') + ":" + ((it.toMillis() / 1000) % 60).toString().padStart(2, '0')
        })

        viewModel.selectedEvent.observe(viewLifecycleOwner, Observer {
            selected_event.text = it.name + "\n" + it.ownerName + "\n" + it.startTime
        })

        viewModel.eventTracking.observe(viewLifecycleOwner, Observer {
            if (!it) {
                selected_event.text = ""
            }

            trackEventSwitch.isChecked = it

        })

        if (viewModel.trackingActive.value == null) {
            viewModel.trackingActive.value = false
        }

        viewModel.distanceMoved.observe(viewLifecycleOwner, Observer {
            distanceMovedView.text =
                BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN).toString() + "m"
        })


        stop_tracking_button.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Stop tracking")
            builder.setMessage("Are you sure you want to stop tracking?")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                trackEventSwitch.isEnabled = true
                stop_tracking_button.isVisible = false
                startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_start_tracking_outline_black_24dp))
                startTrackingButton.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
                liveTrackingSwitch.isEnabled = true
                val intent = Intent(context, LocationService::class.java)
                liveTrackingSwitch.isEnabled = true
                intent.action = "stop"
                context?.unbindService(serviceConnection)
                context?.startService(intent)
                viewModel.StopTracking()
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
            }
            builder.show()

        }

        startTrackingButton.setOnClickListener {
            if (!viewModel.trackingActive.value!!) {
                setupTrackingStart()
                viewModel.trackingActive.value = true
                val intent = Intent(context, LocationService::class.java)
                if (viewModel.trackingStatus.value == TrackingViewModel.TrackingState.PAUSED) {
                    viewModel.StartTracking(true)
                    viewModel.trackingStatus.value = TrackingViewModel.TrackingState.RESUMED
                } else {
                    viewModel.trackingStatus.value = TrackingViewModel.TrackingState.STARTED
                    viewModel.StartTracking(false)
                }
                context?.bindService(
                    Intent(context, LocationService::class.java),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )
                context?.startService(intent)
            } else {
                viewModel.trackingActive.value = false
                setupTrackingPause()
                val intent = Intent(context, LocationService::class.java)
                viewModel.StopTimeTracker(true)
                viewModel.trackingStatus.value = TrackingViewModel.TrackingState.PAUSED
                intent.action = "stop"
                context?.unbindService(serviceConnection)
                context?.startService(intent)
            }
        }
    }

    fun consumeLocation(loc : Location){
        viewModel.ConsumeLocation(loc)
    }


    fun displaySelectEvent(events: List<EventResponse>) {
        val adapter = ActiveEventsAdapter(activity as Activity, events)
        adapter.updateData(events)
        val dialog = AlertDialog.Builder(activity)
            .setAdapter(adapter) { _, which -> viewModel.selectedEvent.value = events[which] }
            .setTitle("Select event").setNegativeButton(
            "Cancel"
        ) { _, _ -> viewModel.eventTracking.value = false }.create()
        val listView = dialog.listView
        listView.divider = ColorDrawable(Color.WHITE)
        listView.dividerHeight = 2
        dialog.show()
    }

    fun setupTrackingStart() {
        startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_tracking_outline_black_24dp))
        startTrackingButton.setBackgroundColor(resources.getColor(R.color.yellow))
        stop_tracking_button.visibility = View.VISIBLE

        trackEventSwitch.isEnabled = false
        liveTrackingSwitch.isEnabled = false
    }

    fun setupTrackingPause() {
        startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_start_tracking_outline_black_24dp))
        startTrackingButton.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
        liveTrackingSwitch.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.trackingActive.value!!) {
            setupTrackingStart()
        } else {
            setupTrackingPause()
        }
    }


}
