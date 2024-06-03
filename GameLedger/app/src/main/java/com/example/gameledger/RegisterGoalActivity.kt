package com.example.gameledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gameledger.databinding.ActivityRegisterGoalBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class RegisterGoalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterGoalBinding
    private lateinit var userService: UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterGoalBinding.inflate(layoutInflater)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.registerButton.setOnClickListener {
            val goalName = binding.goalNameInput.text.toString()
            val gValue = binding.goalValueInput.text.toString()
            val fValue = binding.foodValueInput.text.toString()
            val tValue = binding.trafficValueInput.text.toString()
            val cValue = binding.cultureValueInput.text.toString()
            val lValue = binding.lifeValueInput.text.toString()

            if(goalName.isEmpty()){
                CustomToast.showToast(
                    this@RegisterGoalActivity,
                    "내역 내용을 입력해주세요"
                )
            }else if(gValue.isEmpty() || fValue.isEmpty() || tValue.isEmpty() || cValue.isEmpty() || lValue.isEmpty()){
                CustomToast.showToast(
                    this@RegisterGoalActivity,
                    "금액을 입력해주세요"
                )
            }else {
                val goalValue = Integer.parseInt(gValue)
                val foodValue = Integer.parseInt(fValue)
                val trafficValue = Integer.parseInt(tValue)
                val cultureValue = Integer.parseInt(cValue)
                val lifeValue = Integer.parseInt(lValue)
                userService.signupInputData(
                    goalName,
                    goalValue,
                    foodValue,
                    trafficValue,
                    cultureValue,
                    lifeValue
                )
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
                                            this@RegisterGoalActivity,
                                            message.toString()
                                        )
                                        if (code == 200) {
                                            val intent = Intent(
                                                this@RegisterGoalActivity,
                                                LoginActivity::class.java
                                            )
                                            startActivity(intent)
                                            binding.goalNameInput.text.clear()
                                            binding.goalValueInput.text.clear()
                                            binding.foodValueInput.text.clear()
                                            binding.trafficValueInput.text.clear()
                                            binding.cultureValueInput.text.clear()
                                            binding.lifeValueInput.text.clear()
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    CustomToast.showToast(
                                        this@RegisterGoalActivity,
                                        "응답 내용 없음"
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
        }
    }


}