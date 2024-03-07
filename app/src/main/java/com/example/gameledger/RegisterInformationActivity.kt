package com.example.gameledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gameledger.databinding.ActivityRegisterInformationBinding

class RegisterInformationActivity : AppCompatActivity() {
    lateinit var binding:ActivityRegisterInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}