package com.example.gameledger

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.gameledger.databinding.ActivitySettingsBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var userService: UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        userService = RetrofitClient.retrofit.create(UserService::class.java)
        setContentView(binding.root)
        NavigationBar()

        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

        if (userToken != null) {
            init(userToken)
        }
    }

    private fun init(userToken: String) {

        binding.saveButton.setOnClickListener {
            var password = binding.passwordInput.text.toString()
            var newPassword = binding.newPasswordInput.text.toString()
            var newPasswordAgain = binding.newPasswordAgainInput.text.toString()

            if (newPassword != newPasswordAgain) {
                CustomToast.showToast(
                    this@SettingsActivity,
                    "새로운 비밀번호가 일치하지 않습니다.")
            } else {
                userService.settingsEditData(userToken, password, newPassword)
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
                                            this@SettingsActivity,
                                            message.toString())
                                        if (code == 200) {
                                            val intent = Intent(
                                                this@SettingsActivity,
                                                MainActivity::class.java
                                            )
                                            startActivity(intent)
                                        }
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    CustomToast.showToast(
                                        this@SettingsActivity,
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

        binding.logoutButton.setOnClickListener {
            userService.settingsLogoutData(userToken)
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
                                        this@SettingsActivity,
                                        message.toString())
                                    if (code == 200) {
                                        val context: Context = this@SettingsActivity
                                        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("userToken", "디폴트 값 입니다.")
                                        editor.apply()
                                        val s = sharedPreferences.getString("userToken", "error")
                                        Log.v("userToken", s.toString())
                                        val intent = Intent(
                                            this@SettingsActivity,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            } else {
                                CustomToast.showToast(
                                    this@SettingsActivity,
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

        binding.signoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("설정")
                .setMessage("탈퇴하시겠습니까?")
                .setPositiveButton("예", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "positive")
                        signout(userToken)
                    }
                })
                .setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Log.d("MyTag", "negative")
                    }
                })
                .create()
                .show()

        }

    }

    private fun signout(userToken: String) {
        userService.settingsSignoutData(userToken)
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
                                    this@SettingsActivity,
                                    message.toString())
                                if (code == 200) {
                                    val context: Context = this@SettingsActivity
                                    val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("userToken", "디폴트 값 입니다.")
                                    editor.apply()
                                    val s = sharedPreferences.getString("userToken", "error")
                                    Log.v("userToken", s.toString())
                                    val intent = Intent(
                                        this@SettingsActivity,
                                        LoginActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        } else {
                            CustomToast.showToast(
                                this@SettingsActivity,
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

    fun NavigationBar() {
        val main_button = findViewById<ImageButton>(R.id.main_button)
        main_button.setOnClickListener {
            var intent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val quest_button = findViewById<ImageButton>(R.id.quest_button)
        quest_button.setOnClickListener {
            var intent = Intent(this@SettingsActivity, QuestActivity::class.java)
            startActivity(intent)
        }

        val insert_button = findViewById<ImageButton>(R.id.insert_button)
        insert_button.setOnClickListener {
            var intent = Intent(this@SettingsActivity, InsertActivity::class.java)
            startActivity(intent)
        }

        val showlist_button = findViewById<ImageButton>(R.id.showlist_button)
        showlist_button.setOnClickListener {
            var intent = Intent(this@SettingsActivity, ShowListActivity::class.java)
            startActivity(intent)
        }

        val setting_button = findViewById<ImageButton>(R.id.setting_button)
        setting_button.setOnClickListener {
            var intent = Intent(this@SettingsActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

}


