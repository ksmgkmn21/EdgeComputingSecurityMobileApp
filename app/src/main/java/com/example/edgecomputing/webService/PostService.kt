package com.example.edgecomputing.webService

import com.example.edgecomputing.models.models.ResponseDevice
import com.example.edgecomputing.models.models.StatusReportModel
import com.example.edgecomputing.models.models.user.Device
import com.example.edgecomputing.models.models.user.UserModel
import retrofit2.Call
import retrofit2.http.*

interface PostService {

    @GET("/api/edge/GetInfo/{deviceUUID}")
    fun getStatusReport(
        @Path("deviceUUID") deviceUUID : String,
        @Query("token") token : String
    ): Call<Device>

    @FormUrlEncoded
    @POST("/api/auth/AddDevice")
    fun addDevice(
        @Query("token") token : String,
        @Field("deviceID") deviceID: String,
        @Field("deviceName") deviceName: String
    ): Call<UserModel>

    @FormUrlEncoded
    @POST("/api/auth/SaveUser")
    fun saveUser(
        @Field("email") deviceID: String,
        @Field("password") deviceName: String
    ): Call<UserModel>

    @FormUrlEncoded
    @POST("/api/auth/LoginUser")
    fun login(
        @Field("email") deviceID: String,
        @Field("password") deviceName: String
    ): Call<UserModel>


    @GET("/api/edge/GetDevices")
    fun getDevices(
        @Query("token") token : String
    ): Call<ArrayList<Device>>

    @FormUrlEncoded
    @POST("/api/auth/RemoveDevice")
    fun removeDevice(
        @Query("token") token : String,
        @Field("deviceUUID") deviceUUID: String
    ): Call<UserModel>
}