package com.example.gameledger

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowListActivity : AppCompatActivity() {

    var transactionList = arrayListOf<Transactions>()
    lateinit var transactionAdapter: TransactionAdapter
    lateinit var transactionService: TransactionService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showlist)
        transactionService = RetrofitClient.retrofit.create(TransactionService::class.java)
        InitData()

        val transaction = findViewById<RecyclerView>(R.id.rv_transaction)
        transaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        transaction.setHasFixedSize(true)

        transactionAdapter = TransactionAdapter(transactionList)
        transaction.adapter = transactionAdapter

        val back_button = findViewById<ImageButton>(R.id.back_button)
        back_button.setOnClickListener{
            val intent = Intent(this@ShowListActivity, QuestActivity::class.java)
            startActivity(intent)
        }

    }

    fun InitData(){
        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

        if (userToken != null) {
            transactionService.listInfoData(userToken)
                .enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("StringFormatMatches")
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
                                    Log.d("logging", data.toString())

                                    val total = data.getJSONObject("total")
                                    val expendTotal = total.getInt("expendTotal")
                                    val incomeTotal = total.getInt("incomeTotal")
                                    Log.d("logging", total.toString())
                                    Log.v("expendTotal",expendTotal.toString())
                                    Log.v("incomeTotal",incomeTotal.toString())

                                    val expendTotalTextView: TextView = findViewById(R.id.tv_totalExpense)
                                    val expenseString = resources.getString(R.string.total_placeholder, expendTotal)
                                    expendTotalTextView.text = expenseString

                                    val incomeTotalTextView: TextView = findViewById(R.id.tv_totalIncome)
                                    val incomeString = resources.getString(R.string.total_placeholder, incomeTotal)
                                    incomeTotalTextView.text = incomeString

                                    val sumTotal = incomeTotal - expendTotal
                                    val sumTotalTextView: TextView = findViewById(R.id.tv_totalSum)
                                    val sumString = resources.getString(R.string.total_placeholder, sumTotal)
                                    sumTotalTextView.text = sumString

                                    val list = data.getJSONArray("list")
                                    Log.d("logging", list.toString())

                                    for (i in 0 until list.length()) {
                                        val listItem = list.getJSONObject(i)

                                        val transType = listItem.getBoolean("transType")
                                        val transYear = listItem.getInt("transYear")
                                        val transMonth = listItem.getInt("transMonth")
                                        val transDay = listItem.getInt("transDay")
                                        val transCategory = listItem.getString("transCategory")
                                        val transName = listItem.getString("transName")
                                        val transValue = listItem.getInt("transValue")

                                        Log.v("tranType",transType.toString())
                                        Log.v("transYear",transYear.toString())
                                        Log.v("transMonth",transMonth.toString())
                                        Log.v("transDay",transDay.toString())
                                        Log.v("transCategory",transCategory.toString())
                                        Log.v("transName",transName.toString())
                                        Log.v("transValue",transValue.toString())

                                        val yearTextView: TextView = findViewById(R.id.tv_year)
                                        val yearString = "${transYear}년"
                                        yearTextView.text = yearString

                                        val monthTextView: TextView = findViewById(R.id.tv_month)
                                        val monthString = "${transMonth}월"
                                        monthTextView.text = monthString

                                        val transactions = Transactions(
                                            transType,
                                            transCategory,
                                            "${transYear}. ${transMonth}. ${transDay}",
                                            transName,
                                            transValue
                                        )
                                        transactionList.add(transactions)

                                        val position = transactionList.size-1
                                        transactionAdapter.notifyItemInserted(position)
                                    }

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    // Handle JSON parsing error
                                }
                            } else {
                                Toast.makeText(
                                    this@ShowListActivity,
                                    "응답 내용 없음: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@ShowListActivity,
                                "서버 응답 오류: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 요청이 실패한 경우
                        Toast.makeText(
                            this@ShowListActivity,
                            "네트워크 오류: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
        }
    }
}