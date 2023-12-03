package com.example.gameledger

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import com.example.gameledger.databinding.ActivityInsertBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.s
import java.text.DecimalFormat


class InsertActivity : AppCompatActivity() {
    lateinit var binding: ActivityInsertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        inputData()
    }

    fun init() {
        val valueDecimalFormat = DecimalFormat("#,###")
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            var result:String = ""
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!charSequence.toString().isEmpty() && charSequence.toString() != result) {
                    result = valueDecimalFormat.format(charSequence.toString().replace(",", "").toDouble())
                    binding.valueInputText.setText(result)
                    binding.valueInputText.setSelection(result.length)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        }
        binding.valueInputText.addTextChangedListener(watcher)
        val watcher2: TextWatcher = object : TextWatcher {
            var textlength = 0

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.dateInputText.isFocusable && s.isNotEmpty()) {
                    try {
                        textlength = binding.dateInputText.text.toString().length
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        return
                    }

                    when {
                        textlength == 4 && before != 1 -> {
                            binding.dateInputText.setText(binding.dateInputText.text.toString() + ".")
                            binding.dateInputText.setSelection(binding.dateInputText.text.length)
                        }
                        textlength == 7 && before != 1 -> {
                            binding.dateInputText.setText(binding.dateInputText.text.toString() + ".")
                            binding.dateInputText.setSelection(binding.dateInputText.text.length)
                        }
                        textlength == 5 && !binding.dateInputText.text.toString().contains(".") -> {
                            binding.dateInputText.setText(
                                binding.dateInputText.text.toString().substring(0, 4) + "." +
                                        binding.dateInputText.text.toString().substring(4)
                            )
                            binding.dateInputText.setSelection(binding.dateInputText.text.length)
                        }
                        textlength == 8 && binding.dateInputText.text.toString().substring(7, 8) != "." -> {
                            binding.dateInputText.setText(
                                binding.dateInputText.text.toString().substring(0, 7) + "." +
                                        binding.dateInputText.text.toString().substring(7)
                            )
                            binding.dateInputText.setSelection(binding.dateInputText.text.length)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        binding.dateInputText.addTextChangedListener(watcher2)

    }

    fun inputData() {
        val database = Firebase.database
        val user = database.getReference("users").child("userid")
        user.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var type: Int = 0
                binding.expendRadioButton.setOnClickListener {
                    type = 0
                    Toast.makeText(this@InsertActivity, "지출을 입력합니다.", Toast.LENGTH_SHORT).show()
                }
                binding.incomeRadioButton.setOnClickListener {
                    type = 1
                    Toast.makeText(this@InsertActivity, "수입을 입력합니다.", Toast.LENGTH_SHORT).show()
                }
                binding.cancelButton.setOnClickListener {
                    var intent = Intent(this@InsertActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                binding.inputButton.setOnClickListener {
                    var date = binding.dateInputText.text.toString()
                    var category = binding.categoryInputText.text.toString()
                    var title = binding.titleInputText.text.toString()
                    var value = binding.valueInputText.text.toString()
                    var trans = Transactions(type, category, date, title, value)

                    val newRef = user.child("transactions")
                        .push() // This generates a unique key for the new data
                    newRef.setValue(trans)
                        .addOnSuccessListener {
                            // Data successfully saved
                            // newRef.key contains the unique key generated by push()
                            Log.d("SUCCESS", "Data pushed to Firebase with key: ${newRef.key}")
                        }
                        .addOnFailureListener { e ->
                            // Failed to save data
                            Log.e("FAIL", "Error pushing data to Firebase")
                        }

                    var intent = Intent(this@InsertActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FAIL", "Error fetching data")
            }
        })
    }


}