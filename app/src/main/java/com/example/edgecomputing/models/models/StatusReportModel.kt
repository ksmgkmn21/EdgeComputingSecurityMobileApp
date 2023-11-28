package com.example.edgecomputing.models.models

data class StatusReportModel(
    val __v: Int,
    val _id: String,
    val id: Int,
    val name: String,
    val registrationDate: String,
    val sensors: List<Sensor>
)