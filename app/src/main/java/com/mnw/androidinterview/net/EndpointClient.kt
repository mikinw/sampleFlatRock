package com.mnw.androidinterview.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object EndpointClient {

    private val retrofit: Retrofit by lazy {


        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.itbook.store")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getInstance(): Retrofit {
        return retrofit
    }
}