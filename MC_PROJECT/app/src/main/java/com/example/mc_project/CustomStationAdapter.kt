package com.example.mc_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomStationAdapter(private val stationList: List<String>) : RecyclerView.Adapter<CustomStationAdapter.StationViewHolder>() {

    inner class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationNameTextView: TextView = itemView.findViewById(R.id.stationNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.station_item, parent, false)
        return StationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val currentItem = stationList[position]
        holder.stationNameTextView.text = currentItem
    }

    override fun getItemCount() = stationList.size
}
