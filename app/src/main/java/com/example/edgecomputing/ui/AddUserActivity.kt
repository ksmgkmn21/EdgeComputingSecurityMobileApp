package com.example.edgecomputing.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.edgecomputing.R
import com.example.edgecomputing.databinding.ActivityAddUserBinding
import com.example.edgecomputing.models.models.user.UserModel
import com.example.edgecomputing.webService.ApiClient
import com.example.edgecomputing.webService.PostService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

private lateinit var AddUserBinding: ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    private var PASSWORD_PATTERN: Pattern =
        Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        AddUserBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_add_user
        )
        AddUserBinding.buttonRegister.setOnClickListener {

            register()
        }
    }

    private fun validateEmail(emailInput: String): Boolean {


        if (emailInput.isEmpty()) {
            AddUserBinding.editTextEmailAddress.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            AddUserBinding.editTextEmailAddress.setError("Please enter a valid email address");
            return false;
        } else {
            AddUserBinding.editTextEmailAddress.setError(null);
            return true;
        }
    }


    private fun validatePassword(passwordInput: String): Boolean {
        //String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            AddUserBinding.editTextPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            AddUserBinding.editTextPassword.setError("Password too weak");
            return false;
        } else {
            AddUserBinding.editTextPassword.setError(null);
            return true;
        }
    }

    private fun register() {
        var email = AddUserBinding.editTextEmailAddress.text.toString().trim()
        var password = AddUserBinding.editTextPassword.text.toString().trim()
        var passwordConfirm = AddUserBinding.editTextPasswordConfirm.text.toString().trim()
        if (validateEmail(email) && validatePassword(password) && (password == passwordConfirm)) {
            apiCall(email, password)
        } else if (password != passwordConfirm) {
            AddUserBinding.editTextPasswordConfirm.setError("passwords are not the same");
        }

    }

    private fun apiCall(email: String, password: String) {

        postService = ApiClient.getClient().create(PostService::class.java)
        var post = postService.saveUser(
            email,
            password
        )

        post.enqueue(object : Callback<UserModel> {

            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response?.body() != null) {
                    var user = response.body()!!
                    //var devices = user.user.devices.toTypedArray()
                    val userSharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    val edit = userSharedPreferences.edit()
                    edit.putString("email", email)
                    edit.putString("token", user.token)
                    edit.commit()
                    val intent = Intent(applicationContext, MainActivity::class.java)

                    startActivity(intent)

                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
            }


        })
    }

}