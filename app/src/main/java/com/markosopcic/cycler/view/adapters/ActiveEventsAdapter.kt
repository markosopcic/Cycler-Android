package com.markosopcic.cycler.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.markosopcic.cycler.R
import com.markosopcic.cycler.network.models.EventResponse


class ActiveEventsAdapter(context: Context, var list: List<EventResponse>) :
    ArrayAdapter<EventResponse>(context, R.layout.select_event_item) {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): EventResponse? {
        return list[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    fun customView(position: Int, convertView: View?, parent: ViewGroup): View {
        var customView: View?
        if (convertView == null) {
            customView =
                LayoutInflater.from(context).inflate(R.layout.select_event_item, parent, false)
        } else {
            customView = convertView
        }
        val event = getItem(position)
        customView?.findViewById<TextView>(R.id.event_picker_item_name)?.text =
            "Name: " + event?.name
        customView?.findViewById<TextView>(R.id.event_picker_item_owner_name)?.text =
            "Owner: " + event?.ownerName
        customView?.findViewById<TextView>(R.id.event_picker_item_start_time)?.text =
            "Start Time: " + event?.startTime
        return customView!!

    }

    fun updateData(list: List<EventResponse>) {
        this.list = list
        notifyDataSetChanged()
    }


}