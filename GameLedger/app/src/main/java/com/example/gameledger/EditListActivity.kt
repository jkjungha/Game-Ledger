package com.example.gameledger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameledger.databinding.ActivityEditlistBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditListActivity : AppCompatActivity(), CategoryAdapter.OnItemClickListener {
    lateinit var binding: ActivityEditlistBinding
    lateinit var transactionService: TransactionService
    private lateinit var selectedCategory: String
    private var transId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditlistBinding.inflate(layoutInflater)
        transactionService = RetrofitClient.retrofit.create(TransactionService::class.java)
        setContentView(binding.root)

        val categories = listOf("식비", "교통", "문화", "생활", "기타")
        val categoryRecyclerView: RecyclerView = findViewById(R.id.rv_category)

        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        init() // Type별 레이아웃 변경, Value 포맷 설정
        setData()
        categoryRecyclerView.adapter = CategoryAdapter(categories, selectedCategory, this)
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

    private fun setData() {
        val intent = intent
        val transType = intent.getBooleanExtra("transType", false)
        val transDate = intent.getStringExtra("transDate")
        val transCategory = intent.getStringExtra("transCategory")
        val transName = intent.getStringExtra("transName")
        val transValue = intent.getStringExtra("transValue")
        transId = intent.getIntExtra("transId", 0)

        Log.v("tranType",transType.toString())
        Log.v("transDate",transDate.toString())
        Log.v("transCategory",transCategory.toString())
        Log.v("transName",transName.toString())
        Log.v("transValue",transValue.toString())
        Log.v("tranId",transId.toString())

        if (!transType) {
            binding.expendRadioButton.isChecked = true
            binding.incomeRadioButton.isChecked = false
        }
        else {
            binding.expendRadioButton.isChecked = false
            binding.incomeRadioButton.isChecked = true
        }
        binding.dateInputText.setText(transDate)
        if (transCategory != null) {
            selectedCategory = transCategory
        }
        binding.nameInputText.setText(transName)
        binding.valueInputText.setText(transValue)
    }

    private fun inputData() {

        binding.editButton.setOnClickListener {

            val context: Context = this
            val sharedPreferences = context.getSharedPreferences("saveData",MODE_PRIVATE)
            val userToken = sharedPreferences.getString("userToken","디폴트 값 입니다.")

            val transType = !binding.expendRadioButton.isChecked    //지출: false, 수입: true

            val transDate = binding.dateInputText.text.toString()
            val parts = transDate.split(".")

            val transCategory:String
            if(transType){
                transCategory = "수입"
            }else{
                transCategory = selectedCategory
            }

            val transName = binding.nameInputText.text.toString()

            val value = binding.valueInputText.text.toString()

            if(transName.isEmpty()){
                CustomToast.showToast(
                    this@EditListActivity,
                    "내역 내용을 입력해주세요"
                )
            }else  if(parts.size != 3 || parts[2].length != 2){
                CustomToast.showToast(
                    this@EditListActivity,
                    "날짜를 입력해주세요"
                )
            }else  if(value.isEmpty()){
                CustomToast.showToast(
                    this@EditListActivity,
                    "금액을 입력해주세요"
                )
            }else  {
                val transYear = parts[0].toInt() // 연도
                val transMonth = parts[1].toInt() // 월
                val transDay = parts[2].toInt() // 일
                val transValue = value.replace(",", "").toInt()
                if (userToken != null) {
                    transactionService.listEditData(
                        userToken,
                        transYear,
                        transMonth,
                        transDay,
                        transCategory,
                        transName,
                        transValue,
                        transType,
                        transId
                    )
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d("API Call", "Successful response: ${response.code()}")
                                    // 입력 후 ShowListActivity로 이동
                                    val intent =
                                        Intent(this@EditListActivity, ShowListActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Log.e("API Call", "Unsuccessful response: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                // 통신 실패시 처리
                                CustomToast.showToast(this@EditListActivity, "통신 실패")
                            }
                        })
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            // MainActivity로 이동
            val intent = Intent(this@EditListActivity, ShowListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(category: String) {
        selectedCategory = category
        //CustomToast.showToast(this, "Selected: $category")
    }

}
