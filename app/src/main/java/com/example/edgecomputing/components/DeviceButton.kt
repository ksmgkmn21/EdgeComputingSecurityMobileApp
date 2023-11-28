package com.example.edgecomputing.components

import android.widget.ImageView
import android.widget.TextView

var deviceList = mutableListOf<DeviceButton>()
class DeviceButton(
    val cover: Int,
    var title: String,
    var status: String,
    val id: String?
)
