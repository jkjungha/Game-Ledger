package com.example.gameledger

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.example.gameledger.databinding.ToastLayoutBinding

object CustomToast {

    fun showToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val toastBinding =
            ToastLayoutBinding.inflate(inflater)

        toastBinding.toastText.text = message

        return Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = toastBinding.root
            show()
        }

    }
}