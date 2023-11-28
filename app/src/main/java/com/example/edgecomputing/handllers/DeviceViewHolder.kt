package com.example.edgecomputing.handllers

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edgecomputing.components.DeviceButton
import com.example.edgecomputing.databinding.ButtonDeviceBinding
class DeviceViewHolder(
    private val deviceBinding: ButtonDeviceBinding,
    private val clickListener: DeviceClickListener
): RecyclerView.ViewHolder(deviceBinding.root)
{
    fun bindDevice(device: DeviceButton)
    {
        deviceBinding.btnImage.setImageResource(device.cover)
        deviceBinding.deviceName.text = device.title
        deviceBinding.deviceName.setVisibility(if(device.title == "")  TextView.GONE else TextView.VISIBLE)

        deviceBinding.btnImage.setOnClickListener{
            clickListener.onClick(device)
        }
    }
}