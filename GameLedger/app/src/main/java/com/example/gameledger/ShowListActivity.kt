package com.example.gameledger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

//        InputData(Transactions(1, "월급", "2023.12.04.", "2023년 11월 월급", "800,000원"))
//        val transactionList = arrayListOf(
//            Transactions(1, "월급", "2023.12.04.", "2023년 11월 월급", "800,000원"),
//            Transactions(0, "식비", "2023.12.04.", "학식", "5,000원"),
//            Transactions(0, "식비", "2023.12.04.", "레스티오", "3,000원"),
//            Transactions(0, "교통", "2023.12.04.", "카카오택시", "6,500원"),
//            Transactions(1, "용돈", "2023.12.04.", "할머니용돈", "100,000원"),
//            Transactions(0, "문화", "2023.12.04.", "롯데시네마", "14,000원"),
//            Transactions(0, "식비", "2023.12.04.", "영화관매점", "10,000원"),
//            Transactions(0, "식비", "2023.12.04.", "교촌치킨", "20,000원"),
//            Transactions(0, "문화", "2023.12.04.", "넷플릭스", "10,000원"),
//            Transactions(0, "교통", "2023.12.04.", "버스", "1,500원")
//        )
        InitData()

        val transaction = findViewById<RecyclerView>(R.id.rv_transaction)
        transaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        transaction.setHasFixedSize(true)

        transactionAdapter = TransactionAdapter(transactionList)
        transaction.adapter = transactionAdapter

        val back_button = findViewById<ImageButton>(R.id.back_button)
        back_button.setOnClickListener{
            val intent = Intent(this@ShowListActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun InitData(){
        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

        if (userToken != null) {
            transactionService.totalInfoData(userToken)
                .enqueue(object : Callback<TotalInfo> {
                    override fun onResponse(
                        call: Call<TotalInfo>,
                        response: Response<TotalInfo>
                    ) {
                        if(response.isSuccessful) {
                            val totalInfo = response.body()
                            if (totalInfo != null) {
                                val expendTotal: TextView = findViewById(R.id.tv_totalExpense)
                                val incomeTotal: TextView = findViewById(R.id.tv_totalIncome)
                                val sumTotal: TextView = findViewById(R.id.tv_totalSum)

                                val expenseString = resources.getString(R.string.total_placeholder, totalInfo.expendTotal)
                                val incomeString = resources.getString(R.string.total_placeholder, totalInfo.incomeTotal)

                                val sum = totalInfo.incomeTotal.replace(",", "").toInt() - totalInfo.expendTotal.replace(",", "").toInt()
                                val sumString = resources.getString(R.string.total_placeholder, sum.toString())

                                expendTotal.text = expenseString
                                incomeTotal.text = incomeString
                                sumTotal.text = sumString
                            }
                        } else {
                            Toast.makeText(
                                this@ShowListActivity,
                                "서버 응답 오류: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<TotalInfo>, t: Throwable) {
                        // 요청이 실패한 경우
                        Toast.makeText(
                            this@ShowListActivity,
                            "네트워크 오류: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        if (userToken != null) {
            transactionService.listInfoData(userToken)
                .enqueue(object : Callback<List<TransactionInfo>> {
                    override fun onResponse(
                        call: Call<List<TransactionInfo>>,
                        response: Response<List<TransactionInfo>>
                    ) {
                        if(response.isSuccessful) {
                            val transactionInfoList = response.body()
                            if(transactionInfoList != null) {
                                for(transactionInfo in transactionInfoList) {
                                    val transaction = Transactions(
                                        transactionInfo.transType,
                                        transactionInfo.transCategory,
                                        "${transactionInfo.transYear}-${transactionInfo.transMonth}-${transactionInfo.transDay}",
                                        transactionInfo.transName,
                                        transactionInfo.transValue
                                    )
                                    transactionList.add(transaction)
                                }
                                val insertedPosition = transactionList.size - 1
                                transactionAdapter.notifyItemInserted(insertedPosition)  //데이터셋에 새로운 아이템 추가 -> 뷰 업데이트
                            }
                        } else {
                            Toast.makeText(
                                this@ShowListActivity,
                                "서버 응답 오류: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<List<TransactionInfo>>, t: Throwable) {
                        // 요청이 실패한 경우
                        Toast.makeText(
                            this@ShowListActivity,
                            "네트워크 오류: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

/*
        val database = Firebase.database
        val user = database.getReference("users").child("userid")
        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                transactionList.clear()
                var transactions = dataSnapshot.child("transactions")
                for(trans in transactions.children){
                    val type = trans.child("type").getValue(Int::class.java)
                    val category = trans.child("category").value as? String ?: ""
                    val date = trans.child("date").value as? String ?: ""
                    val title = trans.child("title").value as? String ?: ""
                    val value = trans.child("value").value as? Int ?: ""
                    val t = Transactions((type ?: false) as Boolean, category, date, title,
                        value as Int
                    )
//                    Log.d("DATA", t.type.toString()+" "+ t.category+" "+t.date+" "+t.title+" "+t.value)
                    transactionList.add(t)
                }
                transactionAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FAIL", "Error fetching data")
            }
        })

 */
    }
}