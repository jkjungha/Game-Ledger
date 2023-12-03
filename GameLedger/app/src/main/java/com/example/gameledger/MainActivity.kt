package com.example.gameledger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gameledger.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        init()
    }

    fun init() {
        binding.insertButton.setOnClickListener {
            var intent = Intent(this@MainActivity, InsertActivity::class.java)
            startActivity(intent)
        }
        binding.questButton.setOnClickListener {
            var intent = Intent(this@MainActivity, QuestActivity::class.java)
            startActivity(intent)
        }
        binding.showlistButton.setOnClickListener {
            var intent = Intent(this@MainActivity, ShowListActivity::class.java)
            startActivity(intent)
        }
    }

    fun initData() {
        val database = Firebase.database
        val user = database.getReference("users").child("userid")
        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var value = dataSnapshot.child("savingValue").getValue(Int::class.java).toString().dropLast(4).toInt()
                var current = dataSnapshot.child("currentStage").getValue(Int::class.java).toString().dropLast(4).toInt()
                binding.tvUserName.text = dataSnapshot.key.toString()
                binding.tvSavingGoal.text = dataSnapshot.child("savingGoal").getValue(String::class.java)
                binding.tvSavingValue.text = value.toString()
                binding.tvSavingDegree.text = (current.toFloat()*100/value.toFloat()).toString()
                binding.tvCurrentStage.text = current.toString()
                binding.tvTotalStage.text = value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FAIL", "Error fetching data")
            }
        })

    }


}


