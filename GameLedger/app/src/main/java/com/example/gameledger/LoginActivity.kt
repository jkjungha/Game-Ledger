package com.example.gameledger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gameledger.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userService: UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        init()
    }

    private fun init() {
        binding.loginButton.setOnClickListener {
            var username = binding.idInput.text.toString()
            var password = binding.passwordInput.text.toString()
            val context: Context = this

            userService.loginData(username, password)
                .enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val headers = response.headers()
                            if (headers != null) {
                                val headerValue = headers.get("Authorization")
                                Log.d("HEADER", "Header value ${headerValue}")
                                val sharedPreferences =
                                    context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("userToken", headerValue)
                                editor.apply()
                                Log.e(
                                    "API Call",
                                    "Successful response: ${response.code()}"
                                )
                                val intent = Intent(
                                    this@LoginActivity,
                                    InsertActivity::class.java
                                )
                                startActivity(intent)
                            } else {
                                Log.e(
                                    "API Call",
                                    "Header is null: ${response.code()}"
                                )
                            }

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
        binding.registerButton.setOnClickListener {
            var intent = Intent(
                this@LoginActivity,
                RegisterAuthenticateActivity::class.java
            )
            startActivity(intent)
        }
    }

}