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
import com.markosopcic.cycler.view.adapters.HomeFeedAdapter
import com.markosopcic.cycler.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.android.ext.android.get

class HomeFragment : Fragment() {

    var viewModel = get<HomeViewModel>()
    var adapter: HomeFeedAdapter = HomeFeedAdapter(viewModel.eventFeed.value!!, ::loadMore)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = LinearLayoutManager(this@HomeFragment.activity)
        manager.stackFromEnd = false
        event_feed_recyclerview.layoutManager = manager
        event_feed_recyclerview.adapter = adapter
        viewModel.getMoreEventFeedItems(null)
        viewModel.eventFeed.observe(viewLifecycleOwner, Observer {
            adapter.changeDataset(it)
        })


    }

    fun loadMore() {
        viewModel.getMoreEventFeedItems(::onRefreshCallback)
    }

    fun onRefreshCallback(loadedAny: Boolean) {
        if (!loadedAny) {
            adapter.loadedAll = true
            Toast.makeText(activity, "No more items to load!", Toast.LENGTH_SHORT).show()
        }
    }

}
