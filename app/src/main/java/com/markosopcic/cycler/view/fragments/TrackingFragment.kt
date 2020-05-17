package com.markosopcic.cycler.view.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.markosopcic.cycler.R

class TrackingFragment : Fragment() {

    companion object {
        fun newInstance() = TrackingFragment()
    }

    private lateinit var viewModel: TrackingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tracking_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrackingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
