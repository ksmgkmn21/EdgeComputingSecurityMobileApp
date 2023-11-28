package com.example.edgecomputing.handllers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edgecomputing.components.DeviceButton
import com.example.edgecomputing.databinding.ButtonDeviceBinding

class CardAdapter(private val devices: List<DeviceButton>,
                  private val clickListener: DeviceClickListener)
    : RecyclerView.Adapter<DeviceViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ButtonDeviceBinding.inflate(from, parent, false)
        return DeviceViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bindDevice(devices[position])
    }

    override fun getItemCount(): Int = devices.size
}