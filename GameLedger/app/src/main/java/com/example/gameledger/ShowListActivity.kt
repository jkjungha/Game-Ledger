package com.example.gameledger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ShowListActivity : AppCompatActivity() {

    var transactionList = arrayListOf<Transactions>()
    lateinit var transactionAdapter: TransactionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showlist)

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
            var intent = Intent(this@ShowListActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun InitData(){
        val context: Context = this
        val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
        val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")



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
    }

}