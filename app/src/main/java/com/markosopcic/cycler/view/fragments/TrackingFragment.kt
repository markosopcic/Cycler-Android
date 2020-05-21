package com.markosopcic.cycler.view.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch

import com.markosopcic.cycler.R
import com.markosopcic.cycler.location.LocationService
import com.markosopcic.cycler.viewmodel.TrackingViewModel
import kotlinx.android.synthetic.main.tracking_fragment.*
import org.koin.android.ext.android.get
import org.koin.core.context.KoinContextHandler.get

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
        liveTrackingSwitch.setOnClickListener {
            viewModel.onlineTracking.value = (it as Switch).isChecked
        }
        viewModel.trackingActive.value = false
        stop_tracking_button.setOnClickListener{
            Log.d("click","clack")
        }
        startTrackingButton.setOnClickListener{
            if(!viewModel.trackingActive.value!!){
                setupTrackingStart()
                viewModel.trackingActive.value = true
                val intent = Intent(context,LocationService::class.java)
                intent.putExtra("liveTracking",liveTrackingSwitch.isEnabled)
                intent.putExtra("eventId","");
                context?.startService(intent)


            }else{
                viewModel.trackingActive.value = false
                setupTrackingStop()
                context?.stopService(Intent(context,LocationService::class.java))
            }
        }
    }

    fun setupTrackingStart(){
        startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_tracking_outline_black_24dp))
        stop_tracking_button.visibility = View.VISIBLE
        trackEventSwitch.isEnabled = false
        liveTrackingSwitch.isEnabled = false
    }

    fun setupTrackingStop(){
        startTrackingButton.setImageDrawable(resources.getDrawable(R.drawable.ic_start_tracking_outline_black_24dp))
        stop_tracking_button.visibility = View.INVISIBLE
        trackEventSwitch.isEnabled = true
        liveTrackingSwitch.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.trackingActive.value!!){
            setupTrackingStart()
        }else{
            setupTrackingStop()
        }
    }


}
