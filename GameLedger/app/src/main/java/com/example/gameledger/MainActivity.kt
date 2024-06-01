package com.example.gameledger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.gameledger.databinding.ActivityMainBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var transactionService: TransactionService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        transactionService = RetrofitClient.retrofit.create(TransactionService::class.java)
        initData()
        NavigationBar()
    }

    fun initData(){
        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

        if (userToken != null) {
            transactionService.mainInfoData(userToken)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if(response.isSuccessful) {
                            // Get the response body as a string
                            val responseBodyString = response.body()?.string()

                            // Process the JSON response
                            if (!responseBodyString.isNullOrEmpty()) {
                                try {
                                    // Parse the JSON response string
                                    val jsonObject = JSONObject(responseBodyString)

                                    // Access specific fields from the JSON object
                                    val data = jsonObject.getJSONObject("result")

                                    val username = data.getString("username")
                                    val goalName = data.getString("goalName")
                                    val goalValue = data.getInt("goalValue")
                                    val goalAchieved = data.getInt("goalAchieved")

                                    val usernameTextView: TextView = findViewById(R.id.tv_userName)
                                    val usernameString = "${username}의"
                                    usernameTextView.text = usernameString

                                    val goalNameTextView: TextView = findViewById(R.id.tv_goalName)
                                    goalNameTextView.text = goalName

                                    val goalValueTextView: TextView = findViewById(R.id.tv_goalValue)
                                    val goalValueString = "${goalValue/10000}만원"    // 천원 단위 추가 예정
                                    goalValueTextView.text = goalValueString

                                    var savingDegree = 0.0
                                    if (goalValue != 0) {
                                        savingDegree = goalAchieved.toDouble() / goalValue.toDouble() * 100.0
                                    }
                                    val savingDegreeTextView: TextView = findViewById(R.id.tv_savingDegree)
                                    val savingDegreeString = "${savingDegree}%"
                                    savingDegreeTextView.text = savingDegreeString

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                CustomToast.showToast(
                                    this@MainActivity,
                                    "응답 내용 없음: ${response.code()}")
                            }
                        } else {
                            CustomToast.showToast(
                                this@MainActivity,
                                "서버 응답 오류: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 요청이 실패한 경우
                        CustomToast.showToast(
                            this@MainActivity,
                            "네트워크 오류: ${t.message}")
                    }

                })
        }
    }

    fun NavigationBar() {
        val main_button = findViewById<ImageButton>(R.id.main_button)
        main_button.setOnClickListener {
            var intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val quest_button = findViewById<ImageButton>(R.id.quest_button)
        quest_button.setOnClickListener {
            var intent = Intent(this@MainActivity, QuestActivity::class.java)
            startActivity(intent)
        }

        val insert_button = findViewById<ImageButton>(R.id.insert_button)
        insert_button.setOnClickListener {
            var intent = Intent(this@MainActivity, InsertActivity::class.java)
            startActivity(intent)
        }

        val showlist_button = findViewById<ImageButton>(R.id.showlist_button)
        showlist_button.setOnClickListener {
            var intent = Intent(this@MainActivity, ShowListActivity::class.java)
            startActivity(intent)
        }

        val setting_button = findViewById<ImageButton>(R.id.setting_button)
        setting_button.setOnClickListener {
            var intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


}


