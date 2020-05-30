package com.markosopcic.cycler.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.UserSearchResult

class FriendsAdapter(var onClickUser: ((String) -> Unit)?) :
    RecyclerView.Adapter<FriendsAdapter.UserViewHolder>() {

    private var users: MutableList<UserSearchResult> = mutableListOf()

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_name = itemView.findViewById<TextView>(R.id.user_item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_recycler_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = users[position]
        holder.user_name.text = currentItem.fullName
        holder.itemView.setOnClickListener {
            onClickUser!!(currentItem.id)
        }
    }

    override fun getItemCount() = users.size

    fun changeDataset(list: ArrayList<UserSearchResult>) {
        users.clear()
        users.addAll(list)
        this.notifyDataSetChanged()
    }


}