package com.example.gameledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gameledger.databinding.ActivityRegisterAuthenticateBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class RegisterAuthenticateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterAuthenticateBinding
    private lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAuthenticateBinding.inflate(layoutInflater)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        var type = true
        binding.emailPhoneRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                binding.phoneAuthRadioButton.id -> {
                    binding.authTitle.text = "전화번호 :"
                    binding.authInput.hint = "전화번호"
                    type = true
                }

                binding.emailAuthRadioButton.id -> {
                    binding.authTitle.text = "이메일 :"
                    binding.authInput.hint = "이메일"
                    type = false
                }
            }
        }
        binding.authButton.setOnClickListener {
            val emailPhone = binding.authInput.text.toString()

            userService.signupAuthData(emailPhone, type)
                .enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                "API Call",
                                "Successful response: ${response.code()}"
                            )
//                            TODO("인증처리")
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

        binding.authCodeButton.setOnClickListener {
            val authCode = binding.authCodeInput.text.toString()
            userService.signupAuthCheckData(authCode)
                .enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                "API Call",
                                "Successful response: ${response.code()}"
                            )
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

        binding.nextButton.setOnClickListener {
            val intent = Intent(
                this@RegisterAuthenticateActivity,
                RegisterGoalActivity::class.java
            )
            startActivity(intent)
        }

    }
}