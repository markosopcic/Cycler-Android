package com.markosopcic.cycler.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.EventInvitationResponse
import kotlinx.android.synthetic.main.event_invite.view.*

class EventInvitationsAdapter(
    private var requests: MutableList<EventInvitationResponse>,
    var onAcceptInvitation: ((String, Boolean, Int,((Int) -> Unit)) -> Unit)? = null
) : RecyclerView.Adapter<EventInvitationsAdapter.EventInvitationViewHolder>() {


    class EventInvitationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.event_name
        val invitationId: TextView = itemView.invitation_id
        val startTime: TextView = itemView.event_invitation_start_time
        val acceptButton: ImageButton = itemView.accept_event_invitation_button
        val denyButton: ImageButton = itemView.deny_event_invitation_button
        val senderName: TextView = itemView.event_invitation_sender_name
        val description: TextView = itemView.event_description


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventInvitationViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.event_invite, parent, false)
        return EventInvitationViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: EventInvitationViewHolder, position: Int) {
        val currentItem = requests[position]
        holder.senderName.text =
            "Invited by: " + currentItem.invitedBy.firstName + " " + currentItem.invitedBy.lastName
        holder.startTime.text = "Start time: " + currentItem.eventStartTime
        holder.eventName.text = currentItem.eventName
        holder.invitationId.text = currentItem.invitationId
        holder.description.text = currentItem.eventDescription


        holder.acceptButton.setOnClickListener {
            onAcceptInvitation?.invoke(holder.invitationId.text.toString(), true, position, ::removeItem)
        }

        holder.denyButton.setOnClickListener {
            onAcceptInvitation?.invoke(holder.invitationId.text.toString(), false, position,::removeItem)
        }

    }

    override fun getItemCount() = requests.size

    fun removeItem(index: Int) {
        requests.removeAt(index)
        notifyItemRemoved(index)
    }

    fun changeDataset(list: MutableList<EventInvitationResponse>) {
        requests = list
        notifyDataSetChanged()
    }

}