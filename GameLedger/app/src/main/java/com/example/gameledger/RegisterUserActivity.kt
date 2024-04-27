package com.example.gameledger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gameledger.databinding.ActivityRegisterUserBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterUserBinding
    private lateinit var userService: UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        init()
    }

    private fun init() {
        binding.nextButton.setOnClickListener {
            val username = binding.idRsgInput.text.toString()
            val password = binding.passwordRsgInput.text.toString()
            val againPassword = binding.passwordAgainRsgInput.text.toString()

            userService.signupUserData(username, password, againPassword)
                .enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            var intent = Intent(
                                this@RegisterUserActivity,
                                RegisterGoalActivity::class.java
                            )
                            startActivity(intent)
                        } else {
                            Log.e(
                                "API Call",
                                "Unsuccessful response: ${response.code()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("API Call", "Failed to make API call: ${t.message}", t)
                    }
                })
        }
    }
}