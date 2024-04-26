package com.example.gameledger

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("/login")
    fun fetchLoginData(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>
}