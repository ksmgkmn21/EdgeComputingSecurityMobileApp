package com.example.edgecomputing.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.edgecomputing.R
import com.example.edgecomputing.components.DeviceButton
import com.example.edgecomputing.components.deviceList
import com.example.edgecomputing.databinding.ActivityMainBinding
import com.example.edgecomputing.handllers.CardAdapter
import com.example.edgecomputing.handllers.DeviceClickListener
import com.example.edgecomputing.models.models.ResponseDevice
import com.example.edgecomputing.models.models.Sensor
import com.example.edgecomputing.models.models.StatusReportModel
import com.example.edgecomputing.models.models.user.Device
import com.example.edgecomputing.models.models.user.UserModel
import com.example.edgecomputing.webService.ApiClient
import com.example.edgecomputing.webService.PostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


lateinit var postService: PostService
private lateinit var MainBinding: ActivityMainBinding

class MainActivity : AppCompatActivity(), DeviceClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        deviceList.clear()
        val mainActivity = this
        getDevice(mainActivity)

        MainBinding.logout?.setOnClickListener {

            val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val edit = userSharedPreferences.edit()
            edit.clear()
            edit.commit()
            val intent = Intent(applicationContext, LoginUserActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onClick(device: DeviceButton) {
        if ("0" == device.id) {
            addDevice()

        } else if (device.status == "Owned") {
            val intentMain = Intent(applicationContext, DeviceActivity::class.java)
            intentMain.putExtra("DEVICE_ID", device.id)
            startActivity(intentMain)
        }

    }


    private fun addDevice() {
        val myDialogView = LayoutInflater.from(this).inflate(R.layout.add_device_alert, null)

        var alertDialogBuilder = AlertDialog.Builder(this)
            .setView(myDialogView)
        val alertDialog = alertDialogBuilder.show()

        val buttonAdd = myDialogView.findViewById<Button>(R.id.buttonAdd)
        val editTextChipId = myDialogView.findViewById<EditText>(R.id.editTextChipId)
        val editTextDeviceName = myDialogView.findViewById<EditText>(R.id.editTextDeviceName)
        buttonAdd.setOnClickListener {

            val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val token = userSharedPreferences.getString("token", "default value")
            postService = ApiClient.getClient().create(PostService::class.java)
            var post = postService.addDevice(
                token.toString(),
                editTextChipId.text.toString(),
                editTextDeviceName.text.toString()
            )

            post.enqueue(object : Callback<UserModel> {

                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {

                    if (response.errorBody() == null) {
                        var intent = Intent(applicationContext, MainActivity::class.java);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        //alertDialog.closeOptionsMenu()

                    }
                    else if(response.errorBody() != null)
                    {
                        Toast.makeText(applicationContext, "couldn't add device. Check the chipID", Toast.LENGTH_LONG)
                            .show()
                        var intent = Intent(applicationContext, MainActivity::class.java);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }


            })
        }
    }


    private fun getDevice(viewType: DeviceClickListener) {
        val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val email = userSharedPreferences.getString("email", "default value")
        val token = userSharedPreferences.getString("token", "default value")
        if (email != "default value" && token != "default value") {
            postService = ApiClient.getClient().create(PostService::class.java)
            var post = postService.getDevices(
                token.toString()
            )

            post.enqueue(object : Callback<ArrayList<Device>> {

                override fun onResponse(
                    call: Call<ArrayList<Device>>,
                    response: Response<ArrayList<Device>>
                ) {
                    if (response?.body() != null) {
                        var devices = response.body()!!
                        //var deviceList = devices
                        for (item in devices) {

                            if (item.status == "Owned") {
                                deviceList.add(
                                    DeviceButton(
                                        R.drawable.cloud,
                                        item.name,
                                        item.status,
                                        item._id
                                    )
                                )
                            } else {
                                deviceList.add(
                                    DeviceButton(
                                        R.drawable.cloud,
                                        "waiting device",
                                        item.status,
                                        item._id
                                    )
                                )
                            }
                        }

                        addDeviceList(viewType)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Device>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }


            })
        }
    }

    fun addDeviceList(viewType: DeviceClickListener) {
        val myNewDevice = DeviceButton(R.drawable.add_button, "Add New Device", "", "0")
        deviceList.add(myNewDevice)
        MainBinding.recyclerView?.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = CardAdapter(deviceList, viewType)
        }
    }
}