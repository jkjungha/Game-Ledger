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
    private lateinit var authCode: String
    private var type = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAuthenticateBinding.inflate(layoutInflater)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.emailPhoneRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                binding.phoneAuthRadioButton.id -> {
                    binding.authTitle.text = "전화번호 :"
                    binding.authInput.hint = "전화번호(주의%실제 아는 번호)"
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

                            if (!responseBodyString.isNullOrEmpty()) {
                                try {
                                    val jsonObject = JSONObject(responseBodyString)

                                    val message = jsonObject.getString("message")
                                    val code = jsonObject.getInt("code")
                                    if(code == 200){
                                        CustomToast.showToast(this@RegisterAuthenticateActivity, message.toString())
                                    }else if(code == 400){
                                        CustomToast.showToast(this@RegisterAuthenticateActivity, message.toString())
                                    }
                                    authCode = jsonObject.getJSONObject("result").getInt("authCode").toString()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            } else {
                                CustomToast.showToast(
                                    this@RegisterAuthenticateActivity,
                                    "응답 내용 없음")
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
            val emailPhone = binding.authInput.text.toString()
            val authCode = binding.authCodeInput.text.toString()
            userService.signupAuthCheckData(emailPhone, authCode, type)
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

                            if (!responseBodyString.isNullOrEmpty()) {
                                try {
                                    val jsonObject = JSONObject(responseBodyString)

                                    val message = jsonObject.getString("message")
                                    val code = jsonObject.getInt("code")
                                    if(code == 200){
                                        CustomToast.showToast(this@RegisterAuthenticateActivity, message.toString())
                                    }else if(code == 400){
                                        CustomToast.showToast(this@RegisterAuthenticateActivity, message.toString())
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            } else {
                                CustomToast.showToast(
                                    this@RegisterAuthenticateActivity,
                                    "응답 내용 없음")
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