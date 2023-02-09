package com.mnw.androidinterview.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EndpointClient {

    private val authInterceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .build()
        val request = chain.request().newBuilder()
            .url(url)
        chain.proceed(request.build())
    }

    private val retrofit: Retrofit by lazy {
        //        var mHttpLoggingInterceptor = HttpLoggingInterceptor()
//            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient
            .Builder()
//            .addInterceptor(mHttpLoggingInterceptor)
            .addInterceptor(authInterceptor)
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