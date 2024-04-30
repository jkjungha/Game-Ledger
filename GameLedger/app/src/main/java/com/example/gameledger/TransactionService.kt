package com.example.gameledger

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TransactionService {
    @FormUrlEncoded
    @POST("/input/info")
    fun inputInfoData(
        @Header("token") authorization: String,
        @Field("transYear") transYear: Int,
        @Field("transMonth") transMonth: Int,
        @Field("transDay") transDay: Int,
        @Field("transCategory") transCategory: String,
        @Field("transName") transName: String,
        @Field("transValue") transValue: Int,
        @Field("transType") transType: Boolean
    ): Call<ResponseBody>

    @GET("/list/info")
    fun listInfoData(
        @Header("token") authorization: String
    ): Call<List<TransactionInfo>>

    @GET("/list/info")
    fun totalInfoData(
        @Header("token") authorization: String
    ): Call<TotalInfo>

}