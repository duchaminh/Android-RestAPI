package com.example.restclient.api

import com.example.restclient.model.Authority
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AuthorityService {
    @GET("list_authority")
    fun getListAuthority(@Header("Authorization") authorization: String): Call<ArrayList<Authority>>

    @GET("list_authority/authority_by_name/{authorityName}")
    fun getAuthorityByName(@Header("Authorization") authorization: String, @Path("authorityName") authorityName: String ): Call<Authority>
}