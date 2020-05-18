package com.markosopcic.cycler.view.adapters

import android.system.Os.remove
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.models.FriendRequestResponse
import kotlinx.android.synthetic.main.friend_request.view.*
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRequestsAdapter(private var requests:ArrayList<FriendRequestResponse>,var onAcceptRequest : ((String,Boolean,Int) -> Unit)? = null) : RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestsViewHolder>(){



    class FriendRequestsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val requesterName : TextView = itemView.friend_request_sender_name
        val requestedTime : TextView = itemView.friend_request_sent_time
        val acceptButton : ImageButton = itemView.accept_friend_request_button
        val denyButton : ImageButton = itemView.deny_friend_request_button
        val requesterId : TextView = itemView.friend_request_sender_id

        fun displayError(){
            Toast.makeText(itemView.context,"Something went wrong. Try again!",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_request,parent,false)
        return FriendRequestsViewHolder(itemView)
    }


    override  fun onBindViewHolder(holder: FriendRequestsViewHolder, position: Int) {
        val currentItem = requests[position]
        holder.requesterName.text = currentItem.senderName
        holder.requestedTime.text = currentItem.timeSent.toString()
        holder.requesterId.text = currentItem.sender
        holder.acceptButton.setOnClickListener{
             onAcceptRequest?.invoke(holder.requesterId.text.toString(),true,position)
        }

        holder.denyButton.setOnClickListener{
             onAcceptRequest?.invoke(holder.requesterId.text.toString(),false,position)
        }

    }

    override fun getItemCount() = requests.size

    fun removeItem(index:Int){
        requests.removeAt(index)
        notifyItemRemoved(index)
    }

    fun changeDataset(list: ArrayList<FriendRequestResponse>) {
        requests = list
        notifyDataSetChanged()
    }

}