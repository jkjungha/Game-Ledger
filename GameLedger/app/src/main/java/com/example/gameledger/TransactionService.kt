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
        @Header("Authorization") Authorization: String,
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
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>

    @GET("/main/info")
    fun mainInfoData(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>

    @GET("/quest/info")
    fun questInfoData(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>

    @POST("/quest/reset")
    fun questResetData(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>




}