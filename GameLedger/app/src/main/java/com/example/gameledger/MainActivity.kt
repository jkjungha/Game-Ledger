package com.example.gameledger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

                                    Log.v("username", username.toString())
                                    Log.v("goalName", goalName.toString())
                                    Log.v("goalValue", goalValue.toString())
                                    Log.v("goalAchieved", goalAchieved.toString())

                                    val usernameTextView: TextView = findViewById(R.id.tv_userName)
                                    val usernameString = "${username}의"
                                    usernameTextView.text = usernameString

                                    val goalNameTextView: TextView = findViewById(R.id.tv_goalName)
                                    goalNameTextView.text = goalName

                                    val goalValueTextView: TextView = findViewById(R.id.tv_goalValue)
                                    val goalValueString = "${goalValue/10000}만원"    // 천원 단위 추가 예정
                                    goalValueTextView.text = goalValueString

                                    val savingDegree = goalValue / goalAchieved
                                    val savingDegreeTextView: TextView = findViewById(R.id.tv_savingDegree)
                                    val savingDegreeString = "${savingDegree}%"
                                    savingDegreeTextView.text = savingDegreeString

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "응답 내용 없음: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "서버 응답 오류: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 요청이 실패한 경우
                        Toast.makeText(
                            this@MainActivity,
                            "네트워크 오류: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        }
    }


}


