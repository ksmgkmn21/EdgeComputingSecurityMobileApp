package com.example.edgecomputing.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.edgecomputing.R
import com.example.edgecomputing.components.DeviceButton
import com.example.edgecomputing.components.deviceList
import com.example.edgecomputing.databinding.ActivitySplashScreenBinding
import com.example.edgecomputing.models.models.ResponseDevice
import com.example.edgecomputing.models.models.user.Device
import com.example.edgecomputing.models.models.user.UserModel
import com.example.edgecomputing.webService.ApiClient
import com.example.edgecomputing.webService.PostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingSplashScreenActivityActivity: ActivitySplashScreenBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_splash_screen)
        bindingSplashScreenActivityActivity.cloud.alpha = 0f
        bindingSplashScreenActivityActivity.cloud.animate().setDuration(900).alpha(1f).withEndAction {
            val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
            val email = userSharedPreferences.getString("email", "default value")

            if(email == "default value")
            {
                val intent = Intent(applicationContext, LoginUserActivity::class.java)

                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            }
            else
            {


                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                val token = userSharedPreferences.getString("token", "default value")
                //apiCall(token.toString())
            }

        }
    }

    private fun apiCall(token : String)
    {

        postService = ApiClient.getClient().create(PostService::class.java)
        var post = postService.getDevices(
            token
        )


    }
}