package com.example.gameledger

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("/login")
    fun loginData(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/signup/auth")
    fun signupAuthData(
        @Field("emailPhone") emailPhone: String,
        @Field("type") type: Boolean
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/signup/auth/check")
    fun signupAuthCheckData(
        @Field("emailPhone") emailPhone: String,
        @Field("authCode") authCode: String,
        @Field("type") type: Boolean
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/signup/user")
    fun signupUserData(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("againPassword") againPassword: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/signup/input")
    fun signupInputData(
        @Field("goalName") goalName : String,
        @Field("goalValue") goalValue : Int,
        @Field("foodValue") foodValue : Int,
        @Field("trafficValue") trafficValue : Int,
        @Field("cultureValue") cultureValue : Int,
        @Field("lifeValue") lifeValue : Int
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/settings/edit")
    fun settingsEditData(
        @Header("Authorization") Authorization: String,
        @Field("password") password: String,
        @Field("newPassword") newPassword: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @PATCH("/settings/logout")
    fun settingsLogoutData(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @PATCH("/settings/signout")
    fun settingsSignoutData(
        @Header("Authorization") Authorization: String
    ): Call<ResponseBody>


}

