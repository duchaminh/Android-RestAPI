package com.example.restclient.api

import com.example.restclient.model.AggregateByAuthority
import com.example.restclient.model.User
import com.example.restclient.model.UserForAddAndUpdate
import com.example.restclient.model.UserInfo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface UserService {
    @GET("users")
    fun getUsers(@Header("Authorization") authorization: String): Call<ArrayList<User>>

    @GET("users/search")
    fun search(@Query("familyName") familyName:String?,
               @Query("firstName") firstName:String?,
               @Query("authorityName") authorityName:String?,
               @Header("Authorization") authorization: String)
            :Call<ArrayList<User>>

    @GET("users/{id}")
    fun getUserInfo(@Path("id") id: String, @Header("Authorization") authorization: String): Call<UserInfo>

    @GET("users/shuukei")
    fun getShuukei(@Header("Authorization") authorization: String): Call<ArrayList<AggregateByAuthority>>

    @GET("users/current_user/{token}")
    fun getCurrentUser(@Header("Authorization") authorization: String, @Path("token") token:String): Call<ResponseBody>

    @DELETE("users/{id}")
    fun delete(@Path("id") id: String, @Header("Authorization") authorization: String): Call<ResponseBody>

    @PUT("users/{id}")
    fun update(@Path("id") id: String, @Body user:UserForAddAndUpdate, @Header("Authorization") authorization: String): Call<ResponseBody>

    @POST("users/user")
    fun create(@Body user:UserForAddAndUpdate, @Header("Authorization") authorization: String): Call<UserForAddAndUpdate>
}