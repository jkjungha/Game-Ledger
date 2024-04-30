package com.example.gameledger

import com.google.gson.annotations.SerializedName

data class TransactionInfo(
    @SerializedName("transType") var transType : Boolean,
    @SerializedName("transYear") var transYear : Int,
    @SerializedName("transMonth") var transMonth : Int,
    @SerializedName("transDay") var transDay : Int,
    @SerializedName("transCategory") var transCategory : String,
    @SerializedName("transName") var transName : String,
    @SerializedName("transValue")var transValue : Int
)

data class TotalInfo(
    @SerializedName("expendTotal") var expendTotal : String,
    @SerializedName("incomeTotal") var incomeTotal : String
)