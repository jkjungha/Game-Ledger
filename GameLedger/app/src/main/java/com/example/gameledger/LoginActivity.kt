package com.example.gameledger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gameledger.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        binding.loginButton.setOnClickListener {
            var username = binding.idInput.text
            var password = binding.passwordInput.text

            
        }
    }

}