package com.example.gameledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gameledger.databinding.ActivityRegisterUserBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var userService: UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        setContentView(binding.root)
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
                                    CustomToast.showToast(
                                        this@RegisterUserActivity,
                                        message.toString()
                                    )
                                    if (code == 200) {
                                        val intent = Intent(
                                            this@RegisterUserActivity,
                                            RegisterGoalActivity::class.java
                                        )
                                        startActivity(intent)
                                        binding.idRsgInput.text.clear()
                                        binding.passwordRsgInput.text.clear()
                                        binding.passwordAgainRsgInput.text.clear()
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            } else {
                                CustomToast.showToast(
                                    this@RegisterUserActivity,
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
    }
}