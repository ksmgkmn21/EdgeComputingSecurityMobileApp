package com.example.edgecomputing.models.models.user

data class Sensor(
    val __v: Int,
    val _id: String,
    val device: String,
    val id: Int,
    val reading: Reading,
    val type: String
)