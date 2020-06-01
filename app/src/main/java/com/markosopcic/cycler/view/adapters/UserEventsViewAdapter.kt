package com.markosopcic.cycler.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.UserEventViewResponse

class UserEventsViewAdapter(
    val userId : String,
    private var userEvents: List<UserEventViewResponse>,
    var loadMore: (() -> Unit)? = null, var finishEvent : ((String) -> Unit)
) : RecyclerView.Adapter<UserEventsViewAdapter.UserEventViewViewHolder>() {

    var loadedAll = false

    class UserEventViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameInfoView = itemView.findViewById<TextView>(R.id.event_view_item_name)
        val ownerInfoView = itemView.findViewById<TextView>(R.id.event_view_item_owner)
        val startTimeInfoView = itemView.findViewById<TextView>(R.id.event_view_item_start_time)
        val durationInfoView = itemView.findViewById<TextView>(R.id.event_view_accepted_count)
        val distanceInfoView = itemView.findViewById<TextView>(R.id.event_view_invited_count)
        val finishEventButton = itemView.findViewById<Button>(R.id.event_list_finish_event_button)

    }

    override fun getItemCount(): Int {
        return userEvents.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserEventViewViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.event_view_item, parent, false)
        return UserEventViewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserEventViewViewHolder, position: Int) {
        if(position == userEvents.size -1 && !loadedAll){
            loadMore!!.invoke()
        }
        val item = userEvents[position]
        holder.nameInfoView.text = item.name
        holder.ownerInfoView.text =item.ownerName
        holder.startTimeInfoView.text = "Start: "+item.startTime
        if(item.private){
            val data = item.userEventData.first { it.userId == item.ownerId }
            val hours = ((data.durationSeconds / 60) / 60).toInt()
            val minutes = (data.durationSeconds / 60).toInt() % 60
            val seconds = data.durationSeconds % 60
            holder.durationInfoView.text = hours.toString() + " h " + minutes + " m " + seconds + " s"
            holder.distanceInfoView.text = data.meters.toString()+"m"
            holder.finishEventButton.isEnabled = false
            holder.finishEventButton.isVisible = false
        }else{
            holder.durationInfoView.text = "Invited: "+item.invited
            holder.distanceInfoView.text = "Accepted:" +item.accepted
            if(item.ownerId == userId && !item.finished){
                holder.finishEventButton.isEnabled = true
                holder.finishEventButton.isVisible = true
                holder.finishEventButton.setOnClickListener {
                    finishEvent.invoke(item.id)
                }
            }else{
                holder.finishEventButton.isEnabled = false
                holder.finishEventButton.isVisible = false
            }
        }
    }

    fun finishEvent(id : String){
        userEvents.find {
            it.id == id
        }!!.finished = true
        notifyDataSetChanged()
    }


    fun setDataset(it: List<UserEventViewResponse>?) {
        userEvents = it!!
        notifyDataSetChanged()
    }
}