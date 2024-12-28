package com.example.deneme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SensorDataAdapter(private val sensorList: List<SensorData>) : RecyclerView.Adapter<SensorDataAdapter.SensorViewHolder>() {

    class SensorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestamp: TextView = itemView.findViewById(R.id.txtTimestamp)
        val sensorData: TextView = itemView.findViewById(R.id.txtData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor_data, parent, false)
        return SensorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val currentItem = sensorList[position]
        holder.timestamp.text = currentItem.timestamp
        holder.sensorData.text = currentItem.data
    }

    override fun getItemCount() = sensorList.size
}
