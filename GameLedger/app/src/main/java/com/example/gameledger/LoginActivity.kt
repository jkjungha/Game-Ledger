package com.example.gameledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gameledger.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        init()
    }
    private fun init(){
        binding.loginButton.setOnClickListener {
            var username = binding.idInput.text.toString()
            var password = binding.passwordInput.text.toString()

                apiService.fetchLoginData(username, password)
                    .enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                var intent = Intent(
                                    this@LoginActivity,
                                    RegisterAuthenticateActivity::class.java
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
        binding.registerButton.setOnClickListener{
            var intent = Intent(
                this@LoginActivity,
                RegisterInformationActivity::class.java
            )
            startActivity(intent)
        }
    }

}