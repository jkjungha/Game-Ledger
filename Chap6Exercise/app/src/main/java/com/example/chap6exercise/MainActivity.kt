package com.example.chap6exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chap6exercise.databinding.ActivityMainBinding
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.visibility = View.INVISIBLE
    }
}