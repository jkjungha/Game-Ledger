package com.example.gameledger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameledger.databinding.ToastLayoutBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

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

        LinkData()
        NavigationBar()

//        val back_button = findViewById<ImageButton>(R.id.back_button)
//        back_button.setOnClickListener {
//            var intent = Intent(this@QuestActivity, MainActivity::class.java)
//            startActivity(intent)
//        }
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
                                    Log.v("foodGoal", foodGoal.toString())
                                    Log.v("foodExpend", foodExpend.toString())

                                    val foodQuest = Quests(
                                        "식비",
                                        "하루 식비",
                                        formatNumberWithCommas(foodGoal),
                                        formatNumberWithCommas(foodExpend)
                                    )
                                    questList.add(foodQuest)

                                    val traffic = data.getJSONObject("traffic")
                                    val trafficGoal = traffic.getInt("goal")
                                    val trafficExpend = traffic.getInt("expend")
                                    Log.v("trafficGoal", trafficGoal.toString())
                                    Log.v("trafficExpend", trafficExpend.toString())

                                    val trafficQuest = Quests(
                                        "교통",
                                        "하루 교통비",
                                        formatNumberWithCommas(trafficGoal),
                                        formatNumberWithCommas(trafficExpend)
                                    )
                                    questList.add(trafficQuest)

                                    val culture = data.getJSONObject("culture")
                                    val cultureGoal = culture.getInt("goal")
                                    val cultureExpend = culture.getInt("expend")
                                    Log.v("cultureGoal", cultureGoal.toString())
                                    Log.v("cultureExpend", cultureExpend.toString())

                                    val cultureQuest = Quests(
                                        "문화",
                                        "하루 문화비",
                                        formatNumberWithCommas(cultureGoal),
                                        formatNumberWithCommas(cultureExpend)
                                    )
                                    questList.add(cultureQuest)

                                    val life = data.getJSONObject("life")
                                    val lifeGoal = life.getInt("goal")
                                    val lifeExpend = life.getInt("expend")
                                    Log.v("lifeGoal", lifeGoal.toString())
                                    Log.v("lifeExpend", lifeExpend.toString())

                                    val lifeQuest = Quests(
                                        "생활",
                                        "하루 생활비",
                                        formatNumberWithCommas(lifeGoal),
                                        formatNumberWithCommas(lifeExpend)
                                    )
                                    questList.add(lifeQuest)

                                    questAdapter.notifyDataSetChanged()

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                CustomToast.showToast(
                                    this@QuestActivity,
                                    "응답 내용 없음: ${response.code()}")
                            }
                        } else {
                            CustomToast.showToast(
                                this@QuestActivity,
                                "서버 응답 오류: ${response.code()}")
                        }
                    }

                    private fun formatNumberWithCommas(number: Int): String {
                        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                        return numberFormat.format(number)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 요청이 실패한 경우
                        CustomToast.showToast(
                            this@QuestActivity,
                            "네트워크 오류: ${t.message}")
                    }

                })
        }
    }

    fun LinkData() {
        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

        val link_button = findViewById<ImageButton>(R.id.link_button)
        link_button.setOnClickListener {
            if (userToken != null) {
                transactionService.questResetData(userToken)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            Log.d("Logging","click link_button")
                            if (response.isSuccessful) {
                                Log.d("API Call", "Successful response: ${response.code()}")
                                // 입력 후 MainActivity로 이동
                                val intent = Intent(this@QuestActivity, QuestActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.e("API Call", "Unsuccessful response: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // 통신 실패시 처리
                            CustomToast.showToast(this@QuestActivity, "통신 실패")
                        }
                    })
            }
        }
    }

    fun NavigationBar() {
        val main_button = findViewById<ImageButton>(R.id.main_button)
        main_button.setOnClickListener {
            var intent = Intent(this@QuestActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val quest_button = findViewById<ImageButton>(R.id.quest_button)
        quest_button.setOnClickListener {
            var intent = Intent(this@QuestActivity, QuestActivity::class.java)
            startActivity(intent)
        }

        val insert_button = findViewById<ImageButton>(R.id.insert_button)
        insert_button.setOnClickListener {
            var intent = Intent(this@QuestActivity, InsertActivity::class.java)
            startActivity(intent)
        }

        val showlist_button = findViewById<ImageButton>(R.id.showlist_button)
        showlist_button.setOnClickListener {
            var intent = Intent(this@QuestActivity, ShowListActivity::class.java)
            startActivity(intent)
        }

        val setting_button = findViewById<ImageButton>(R.id.setting_button)
        setting_button.setOnClickListener {
            var intent = Intent(this@QuestActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


}



