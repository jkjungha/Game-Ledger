package com.example.gameledger

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gameledger.databinding.ActivityInsertBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class InsertActivity : AppCompatActivity() {
    lateinit var binding: ActivityInsertBinding
    lateinit var transactionService: TransactionService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        transactionService = RetrofitClient.retrofit.create(TransactionService::class.java)
        setContentView(binding.root)
        init()
        inputData()
    }

    fun init() {
        binding.typeRadioGroup.setOnCheckedChangeListener {radioGroup, checkedID ->
            when(checkedID) {
                binding.incomeRadioButton.id -> {
                    binding.linearLayout2.visibility = View.GONE
                }
                binding.expendRadioButton.id -> {
                    binding.linearLayout2.visibility = View.VISIBLE
                }
            }
        }
        val valueDecimalFormat = DecimalFormat("#,###")
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            var result: String = ""
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!charSequence.toString().isEmpty() && charSequence.toString() != result) {
                    result = valueDecimalFormat.format(
                        charSequence.toString().replace(",", "").toDouble()
                    )
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

                        textlength == 8 && binding.dateInputText.text.toString()
                            .substring(7, 8) != "." -> {
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
        binding.inputButton.setOnClickListener {
            val transDate = binding.dateInputText.text.toString()
            val parts = transDate.split(".")
            if (parts.size == 3) {
            }
            val transYear = parts[0].toInt() // 연도
            val transMonth = parts[1].toInt() // 월
            val transDay = parts[2].toInt() // 일

            var transCategory = binding.categoryInputText.text.toString()
            val transName = binding.titleInputText.text.toString()

            var value = binding.valueInputText.text.toString()
            val transValue = value.replace(",", "").toInt()

            val transType = !binding.expendRadioButton.isChecked    //지출: false, 수입: true

            val trans = Transactions(transType, transCategory, transDate, transName, transValue)

            transactionService.inputInfoData(
                "token",
                transYear,
                transMonth,
                transDay,
                transCategory,
                transName,
                transValue,
                transType
            )
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("API Call", "Successful response: ${response.code()}")
                            // 입력 후 MainActivity로 이동
                            val intent = Intent(this@InsertActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.e("API Call", "Unsuccessful response: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 통신 실패시 처리
                        Toast.makeText(this@InsertActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.cancelButton.setOnClickListener {
            // MainActivity로 이동
            val intent = Intent(this@InsertActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

        /*
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
*/