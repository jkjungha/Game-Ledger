package com.example.gameledger

import com.google.gson.annotations.SerializedName

data class TransactionInfo(
    @SerializedName("transType") val transType : Boolean,
    @SerializedName("transYear") val transYear : Int,
    @SerializedName("transMonth") val transMonth : Int,
    @SerializedName("transDay") val transDay : Int,
    @SerializedName("transCategory") val transCategory : String,
    @SerializedName("transName") val transName : String,
    @SerializedName("transValue")val transValue : Int
)

data class TotalInfo(
    @SerializedName("expendTotal") val expendTotal : String,
    @SerializedName("incomeTotal") val incomeTotal : String
)
