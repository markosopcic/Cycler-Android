package com.markosopcic.cycler.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.EventFeedResponse

class HomeFeedAdapter(
    private var feedItems: List<EventFeedResponse>,
    var loadMore: (() -> Unit)? = null
) : RecyclerView.Adapter<HomeFeedAdapter.EventFeedItemViewHolder>() {

    var loadedAll = false

    class EventFeedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameInfoView = itemView.findViewById<TextView>(R.id.feed_item_name)
        val ownerInfoView = itemView.findViewById<TextView>(R.id.feed_item_owner)
        val startTimeInfoView = itemView.findViewById<TextView>(R.id.feed_item_start_time)
        val durationInfoView = itemView.findViewById<TextView>(R.id.feed_item_duration)
        val distanceInfoView = itemView.findViewById<TextView>(R.id.feed_item_distance)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventFeedItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.event_feed_item, parent, false)
        return EventFeedItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return feedItems.size
    }

    override fun onBindViewHolder(holder: EventFeedItemViewHolder, position: Int) {
        if (!loadedAll && position == feedItems.size - 1) {
            loadMore?.invoke()
        }
        val item = feedItems[position]
        holder.nameInfoView.text = item.name
        holder.ownerInfoView.text = item.user
        holder.startTimeInfoView.text = item.startTime
        holder.distanceInfoView.text = item.meters.toString() + "m"
        val hours = ((item.duration / 60) / 60).toInt()
        val minutes = (item.duration / 60).toInt() % 60
        val seconds = item.duration % 60
        holder.durationInfoView.text = hours.toString() + " h " + minutes + " m " + seconds + " s"
    }

    fun changeDataset(data: List<EventFeedResponse>) {
        this.feedItems = data
        notifyDataSetChanged()
    }


}