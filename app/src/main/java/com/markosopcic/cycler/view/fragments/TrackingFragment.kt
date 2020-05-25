package com.markosopcic.cycler.view.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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


class TrackingFragment : Fragment() {


    val viewModel = get<TrackingViewModel>()

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
            if(it.isChecked){
                viewModel.refreshActiveEvents(::displaySelectEvent)
            }
        }

        viewModel.selectedEvent.observe(viewLifecycleOwner, Observer {
            selected_event.text = it.name+"\n"+it.ownerName+"\n"+it.startTime
        })

        viewModel.eventTracking.observe(viewLifecycleOwner, Observer {
            if(!it){
                selected_event.text = ""
            }

            trackEventSwitch.isChecked = it

        })

        if(viewModel.trackingActive.value == null){
            viewModel.trackingActive.value = false
        }

        stop_tracking_button.setOnClickListener{
            Log.d("click","clack")

            trackEventSwitch.isEnabled = true
            stop_tracking_button.isVisible = false
            startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_start_tracking_outline_black_24dp))
            startTrackingButton.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
            liveTrackingSwitch.isEnabled = true
            val intent = Intent(context,LocationService::class.java)
            intent.action = "stop"
            viewModel.trackingStatus.value = TrackingViewModel.TrackingState.STOPPED
            context?.startService(intent)
        }

        viewModel.trackingStatus.observe(viewLifecycleOwner, Observer {
            Log.d("State",it.toString())
        })
        startTrackingButton.setOnClickListener{
            if(!viewModel.trackingActive.value!!){
                setupTrackingStart()
                viewModel.trackingActive.value = true
                val intent = Intent(context,LocationService::class.java)
                if(viewModel.trackingStatus.value == TrackingViewModel.TrackingState.PAUSED){
                    intent.action = "resume"
                    viewModel.trackingStatus.value = TrackingViewModel.TrackingState.RESUMED
                }else{
                    intent.action = "start"
                    viewModel.trackingStatus.value = TrackingViewModel.TrackingState.STARTED
                }

                intent.putExtra("liveTracking",liveTrackingSwitch.isChecked)
                intent.putExtra("eventTracking",trackEventSwitch.isChecked)
                if(trackEventSwitch.isChecked){
                    intent.putExtra("trackedEventId",viewModel.selectedEvent.value?.id)
                }
                context?.startService(intent)
            }else{
                viewModel.trackingActive.value = false
                setupTrackingPause()
                val intent = Intent(context,LocationService::class.java)
                intent.action = "pause"
                viewModel.trackingStatus.value = TrackingViewModel.TrackingState.PAUSED
                context?.startService(intent)
            }
        }
    }


    fun displaySelectEvent(events : List<EventResponse>){
        val adapter = ActiveEventsAdapter(activity as Activity,events)
        adapter.updateData(events!!)
        val dialog = AlertDialog.Builder(activity).setAdapter(adapter) { _, which -> viewModel.selectedEvent.value = events!![which] }.setTitle("Select event").setNegativeButton("Cancel"
        ) { _, _ -> viewModel.eventTracking.value = false}.create()
        val listView = dialog.listView
        listView.divider =  ColorDrawable(Color.WHITE)
        listView.dividerHeight = 2
        dialog.show()
    }

    fun setupTrackingStart(){
        startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_tracking_outline_black_24dp))
        startTrackingButton.setBackgroundColor(resources.getColor(R.color.yellow))
        stop_tracking_button.visibility = View.VISIBLE

        trackEventSwitch.isEnabled = false
        liveTrackingSwitch.isEnabled = false
    }

    fun setupTrackingPause(){
        startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_start_tracking_outline_black_24dp))
        startTrackingButton.setBackgroundColor(resources.getColor(R.color.holo_green_dark))
        liveTrackingSwitch.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.trackingActive.value!!){
            setupTrackingStart()
        }else{
            setupTrackingPause()
        }
    }


}
