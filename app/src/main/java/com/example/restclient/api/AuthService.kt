package com.example.restclient.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("auth")
    fun getToken(
        @Field("username") username:String,
        @Field("password") password:String
    ):Call<ResponseBody>

}