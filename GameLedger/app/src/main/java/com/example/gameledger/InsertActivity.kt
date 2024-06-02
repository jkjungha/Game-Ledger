package com.example.gameledger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameledger.databinding.ActivityInsertBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class InsertActivity : AppCompatActivity(), CategoryAdapter.OnItemClickListener {
    lateinit var binding: ActivityInsertBinding
    private lateinit var transactionService: TransactionService
    private lateinit var selectedCategory: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        transactionService = RetrofitClient.retrofit.create(TransactionService::class.java)
        setContentView(binding.root)

        val categories = listOf("식비", "교통", "문화", "생활", "기타")
        val categoryRecyclerView: RecyclerView = findViewById(R.id.rv_category)

        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        selectedCategory = categories.first()
        categoryRecyclerView.adapter = CategoryAdapter(categories, selectedCategory, this)

        init()
        inputData()
    }

    private fun init() {
        binding.typeRadioGroup.setOnCheckedChangeListener { _, checkedID ->
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
                if (charSequence.toString().isNotEmpty() && charSequence.toString() != result) {
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

    private fun inputData() {


        binding.inputButton.setOnClickListener {

            val context: Context = this
            val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
            val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

            val transDate = binding.dateInputText.text.toString()
            val parts = transDate.split(".")
//            if (parts.size == 3) {
//            }
            val transType = !binding.expendRadioButton.isChecked    //지출: false, 수입: true

            val transYear = parts[0].toInt() // 연도
            val transMonth = parts[1].toInt() // 월
            val transDay = parts[2].toInt() // 일

            val transCategory:String
            if(transType){
                transCategory = "수입"
            }else{
                transCategory = selectedCategory
            }

            val transName = binding.titleInputText.text.toString()

            var value = binding.valueInputText.text.toString()
            val transValue = value.replace(",", "").toInt()


            //val trans = Transactions(transType, transCategory, transDate, transName, transValue)

            if (userToken != null) {
                transactionService.inputInfoData(
                    userToken,
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
                                // 입력 후 ShowListActivity로 이동
                                val intent = Intent(this@InsertActivity, ShowListActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.e("API Call", "Unsuccessful response: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // 통신 실패시 처리
                            CustomToast.showToast(this@InsertActivity, "통신 실패")
                        }
                    })
            }
        }

        binding.cancelButton.setOnClickListener {
            // MainActivity로 이동
            val intent = Intent(this@InsertActivity, ShowListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(category: String) {
        selectedCategory = category
        //CustomToast.showToast(this, "Selected: $category")
    }
}
