package com.example.edgecomputing.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.edgecomputing.R
import com.example.edgecomputing.databinding.ActivityDeviceBinding
import com.example.edgecomputing.models.models.StatusReportModel
import com.example.edgecomputing.models.models.user.Device
import com.example.edgecomputing.models.models.user.UserModel
import com.example.edgecomputing.webService.ApiClient
import com.example.edgecomputing.webService.PostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var Devicebinding: ActivityDeviceBinding
class DeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Devicebinding= DataBindingUtil.setContentView(
            this, R.layout.activity_device)
        val deviceId =intent.getStringExtra("DEVICE_ID")
        showStatusReport(deviceId.toString())

        Devicebinding.progressBarAirQuality.setProgress(100, false)
        Devicebinding.backButton.setOnClickListener{
            finish()
        }
        Devicebinding.reloadButton.setOnClickListener{
            showStatusReport(deviceId.toString())
        }

        Devicebinding.removeBtn.setOnClickListener{

            val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val email = userSharedPreferences.getString("email", "default value")
            val token = userSharedPreferences.getString("token", "default value")
            postService = ApiClient.getClient().create(PostService::class.java)
            var post = postService.removeDevice(
                token.toString(),
                deviceId.toString()
            )

            post.enqueue(object : Callback<UserModel> {

                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

                    if (response?.body() != null) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else if(response.errorBody() != null)
                    {
                        Toast.makeText(applicationContext, "couldn't delete device", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
                }


            })
        }

    }

    private fun showStatusReport(id: String) {
        val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = userSharedPreferences.getString("email", "default value")
        val token = userSharedPreferences.getString("token", "default value")

        postService = ApiClient.getClient().create(PostService::class.java)
        var post = postService.getStatusReport(
            id,
            token.toString()
        )

        post.enqueue(object : Callback<Device> {

            override fun onResponse(call: Call<Device>, response: Response<Device>) {

                if (response?.body() != null) {
                    var device = response.body()!!
                    Devicebinding.deviceName.text = device.name;
                    var tempreture = device.sensors[0].reading.value
                    Devicebinding.textTempreture.text = "$tempreture" + "ºC"
                    var hum = device.sensors[1].reading.value
                    Devicebinding.textHumanity.text= "$hum" + "g.m^-3"
                    var air = device.sensors[2].reading.value
                    Devicebinding.textAirQuality.text= "$air" +"ppm"
                    Devicebinding.progressBarTempreture.setProgress(tempreture.toInt(), true)
                    Devicebinding.progressBarHumanity.setProgress(hum.toInt(), true)
                    Devicebinding.progressBarAirQuality.setProgress(air.toInt(), true)
                }
            }

            override fun onFailure(call: Call<Device>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
            }


        })
    }

}