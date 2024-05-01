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
                    binding.phoneAuthRadioButton.setBackgroundResource(R.drawable.phone_button_on_style)
                    binding.emailAuthRadioButton.setBackgroundResource(R.drawable.email_button_off_style)
                    binding.phoneAuthRadioButton.setTextColor(resources.getColor(R.color.black))
                    binding.emailAuthRadioButton.setTextColor(resources.getColor(R.color.gray))
                    type = true
                }

                binding.emailAuthRadioButton.id -> {
                    binding.authTitle.text = "이메일 :"
                    binding.authInput.hint = "이메일"
                    binding.phoneAuthRadioButton.setBackgroundResource(R.drawable.phone_button_off_style)
                    binding.emailAuthRadioButton.setBackgroundResource(R.drawable.email_button_on_style)
                    binding.phoneAuthRadioButton.setTextColor(resources.getColor(R.color.gray))
                    binding.emailAuthRadioButton.setTextColor(resources.getColor(R.color.black))
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
                RegisterUserActivity::class.java
            )
            startActivity(intent)
        }

    }
}