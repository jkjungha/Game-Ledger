package com.example.gameledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QuestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest)

        val questList = arrayListOf(
            Quests("식비", "하루 식비", 50000, 30000)
        )

        val quest = findViewById<RecyclerView>(R.id.rv_quest)
        quest.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        quest.setHasFixedSize(true)

        quest.adapter = QuestAdapter(questList)
    }
}


