package com.example.edgecomputing.models.models.user

data class Device (
    val __v: Int,
    val _id: String,
    val id: String,
    val name: String,
    val registrationDate: String,
    val sensors: List<Sensor>,
    val status: String
)