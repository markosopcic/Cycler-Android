package com.markosopcic.cycler.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.markosopcic.cycler.R
import com.markosopcic.cycler.view.adapters.UserEventsViewAdapter
import com.markosopcic.cycler.viewmodel.EventViewViewModel
import kotlinx.android.synthetic.main.events_view_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.ext.android.get

class EventsViewFragment : Fragment(){

    var viewModel = get<EventViewViewModel>()
    var adapter :UserEventsViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    return inflater.inflate(R.layout.events_view_fragment, container, false)
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.feedItems.value = mutableListOf()
    adapter = UserEventsViewAdapter(mutableListOf(),::loadMoreEvents )
   events_view_recyclerview.adapter = adapter
    val manager = LinearLayoutManager(this.activity)
    manager.stackFromEnd = false
    events_view_recyclerview.layoutManager = manager
    viewModel.feedItems.observe(viewLifecycleOwner, Observer {
        adapter!!.setDataset(it)
    })

    viewModel.loadMoreUserEvents(::onLoadMoreCallback)
}

fun loadMoreEvents(){
    viewModel.loadMoreUserEvents(::onLoadMoreCallback)
}

    fun onLoadMoreCallback(loadedAny : Boolean){
        if(!loadedAny){
            adapter!!.loadedAll = true
            Toast.makeText(activity, "No more items to load!", Toast.LENGTH_SHORT).show()
        }
    }

}