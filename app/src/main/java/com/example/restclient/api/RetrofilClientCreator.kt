package com.example.restclient.api

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofilClientCreator {
    private const val BASE_URL = "http://192.168.5.106:8080/traning/rest/"

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) //This is the onlt mandatory call on Builder object
            .addConverterFactory(JacksonConverterFactory.create()) // Convertor library used to convert response into POJO
            // .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
