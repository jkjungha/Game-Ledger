package com.example.gameledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gameledger.databinding.ActivityRegisterAuthenticateBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
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
        var type = false
        binding.emailPhoneRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                binding.phoneAuthRadioButton.id -> {
                    binding.authTitle.text = "전화번호 :"
                    binding.authInput.hint = "전화번호"
                    binding.phoneAuthRadioButton.setBackgroundResource(R.drawable.phone_button_on_style)
                    binding.emailAuthRadioButton.setBackgroundResource(R.drawable.email_button_off_style)
                    binding.phoneAuthRadioButton.setTextColor(resources.getColor(R.color.black))
                    binding.emailAuthRadioButton.setTextColor(resources.getColor(R.color.gray))
                    type = false
                }

                binding.emailAuthRadioButton.id -> {
                    binding.authTitle.text = "이메일 :"
                    binding.authInput.hint = "이메일"
                    binding.phoneAuthRadioButton.setBackgroundResource(R.drawable.phone_button_off_style)
                    binding.emailAuthRadioButton.setBackgroundResource(R.drawable.email_button_on_style)
                    binding.phoneAuthRadioButton.setTextColor(resources.getColor(R.color.gray))
                    binding.emailAuthRadioButton.setTextColor(resources.getColor(R.color.black))
                    type = true
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
                            val responseBodyString = response.body()?.string()

                            // Process the JSON response
                            if (!responseBodyString.isNullOrEmpty()) {
                                try {
                                    // Parse the JSON response string
                                    val jsonObject = JSONObject(responseBodyString)

                                    // Access specific fields from the JSON object
                                    val message = jsonObject.getString("message")
                                    val code = jsonObject.getInt("code")
                                    Toast.makeText(this@RegisterAuthenticateActivity, message.toString(), Toast.LENGTH_SHORT).show()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                Toast.makeText(
                                    this@RegisterAuthenticateActivity,
                                    "응답 내용 없음",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                            val responseBodyString = response.body()?.string()

                            // Process the JSON response
                            if (!responseBodyString.isNullOrEmpty()) {
                                try {
                                    // Parse the JSON response string
                                    val jsonObject = JSONObject(responseBodyString)

                                    // Access specific fields from the JSON object
                                    val message = jsonObject.getString("message")
                                    val code = jsonObject.getInt("code")
                                    Toast.makeText(this@RegisterAuthenticateActivity, message.toString(), Toast.LENGTH_SHORT).show()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                Toast.makeText(
                                    this@RegisterAuthenticateActivity,
                                    "응답 내용 없음",
                                    Toast.LENGTH_SHORT
                                ).show()
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

        binding.nextButton.setOnClickListener {
            val intent = Intent(
                this@RegisterAuthenticateActivity,
                RegisterUserActivity::class.java
            )
            startActivity(intent)
        }

    }
}