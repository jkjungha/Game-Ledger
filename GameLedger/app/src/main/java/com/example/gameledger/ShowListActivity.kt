package com.example.gameledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showlist)

        val transactionList = arrayListOf(
            Transactions(R.drawable.treasure_opened, "월급", "2023.12.04.", "2023년 11월 월급", "800,000원"),
            Transactions(R.drawable.treasure_closed, "식비", "2023.12.04.", "학식", "5,000원"),
            Transactions(R.drawable.treasure_closed, "식비", "2023.12.04.", "레스티오", "3,000원"),
            Transactions(R.drawable.treasure_closed, "교통", "2023.12.04.", "카카오택시", "6,500원"),
            Transactions(R.drawable.treasure_opened, "용돈", "2023.12.04.", "할머니용돈", "100,000원"),
            Transactions(R.drawable.treasure_closed, "문화", "2023.12.04.", "롯데시네마", "14,000원"),
            Transactions(R.drawable.treasure_closed, "식비", "2023.12.04.", "영화관매점", "10,000원"),
            Transactions(R.drawable.treasure_closed, "식비", "2023.12.04.", "교촌치킨", "20,000원"),
            Transactions(R.drawable.treasure_closed, "문화", "2023.12.04.", "넷플릭스", "10,000원"),
            Transactions(R.drawable.treasure_closed, "교통", "2023.12.04.", "버스", "1,500원")
        )

        val transaction = findViewById<RecyclerView>(R.id.rv_transaction)
        transaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        transaction.setHasFixedSize(true)

        transaction.adapter = TransactionAdapter(transactionList)
    }
}