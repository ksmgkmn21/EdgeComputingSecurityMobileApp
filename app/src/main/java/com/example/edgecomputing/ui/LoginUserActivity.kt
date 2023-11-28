package com.example.edgecomputing.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.edgecomputing.R
import com.example.edgecomputing.databinding.ActivityLoginUserBinding
import com.example.edgecomputing.models.models.user.Device
import com.example.edgecomputing.models.models.user.UserModel
import com.example.edgecomputing.webService.ApiClient
import com.example.edgecomputing.webService.PostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private lateinit var LoginUserBinding: ActivityLoginUserBinding
class LoginUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        LoginUserBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login_user)


        LoginUserBinding.textViewRegister.setOnClickListener{
            val intent = Intent(applicationContext, AddUserActivity::class.java)
            //intent.putExtra()
            startActivity(intent)
        }

        LoginUserBinding.buttonLogin.setOnClickListener{
            login()
        }
    }

    private fun login()
    {
        var email = LoginUserBinding.loginEmail.text.toString().trim()
        var password = LoginUserBinding.loginPassword.text.toString().trim()
        apiCall(email, password)
    }
    private fun apiCall(email : String, password : String)
    {
        postService = ApiClient.getClient().create(PostService::class.java)
        var post = postService.login(
            email,
            password
        )

        post.enqueue(object : Callback<UserModel> {

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

                if (response?.body() != null) {
                    var user = response.body()!!

                    val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    val edit = userSharedPreferences.edit()
                    edit.putString("email", email)
                    edit.putString("token", user.token)
                    edit.commit()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)


                }
                else
                {
                    Toast.makeText(applicationContext, "couldn't login", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
            }


        })
    }
}