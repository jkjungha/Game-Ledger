package com.example.gameledger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestActivity : AppCompatActivity() {
    var questList = arrayListOf<Quests>()
    lateinit var questAdapter: QuestAdapter
    lateinit var transactionService: TransactionService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)
        transactionService = RetrofitClient.retrofit.create(TransactionService::class.java)
        InitData()

        val quest = findViewById<RecyclerView>(R.id.rv_quest)
        quest.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        quest.setHasFixedSize(true)

        questAdapter = QuestAdapter(questList)
        quest.adapter = questAdapter

        val back_button = findViewById<ImageButton>(R.id.back_button)
        back_button.setOnClickListener {
            var intent = Intent(this@QuestActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun InitData(){
        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

        if (userToken != null) {
            transactionService.questInfoData(userToken)
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

                                    val food = data.getJSONObject("food")
                                    val foodGoal = food.getInt("goal")
                                    val foodExpend = food.getInt("expend")

                                    val traffic = data.getJSONObject("traffic")
                                    val trafficGoal = food.getInt("goal")
                                    val trafficExpend = food.getInt("expend")

                                    val culture = data.getJSONObject("culture")
                                    val cultureGoal = food.getInt("goal")
                                    val cultureExpend = food.getInt("expend")

                                    val life = data.getJSONObject("life")
                                    val lifeGoal = food.getInt("goal")
                                    val lifeExpend = food.getInt("expend")

                                    Log.v("foodGoal", foodGoal.toString())
                                    Log.v("foodExpend", foodExpend.toString())
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                Toast.makeText(
                                    this@QuestActivity,
                                    "응답 내용 없음: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@QuestActivity,
                                "서버 응답 오류: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 요청이 실패한 경우
                        Toast.makeText(
                            this@QuestActivity,
                            "네트워크 오류: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        }
    }



}



