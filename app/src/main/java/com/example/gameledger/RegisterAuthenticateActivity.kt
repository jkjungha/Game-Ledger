package com.example.gameledger

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gameledger.databinding.ActivityRegisterAuthenticateBinding

class RegisterAuthenticateActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterAuthenticateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAuthenticateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}