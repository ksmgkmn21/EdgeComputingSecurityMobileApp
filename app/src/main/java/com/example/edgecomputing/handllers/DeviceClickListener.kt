package com.example.edgecomputing.handllers

import com.example.edgecomputing.components.DeviceButton

interface DeviceClickListener {
    fun onClick(device: DeviceButton)
}