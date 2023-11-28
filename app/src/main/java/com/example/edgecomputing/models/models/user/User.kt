package com.example.edgecomputing.models.models.user

data class User(
    val __v: Int,
    val _id: String,
    val devices: List<Device>,
    val email: String,
    val password: String
)