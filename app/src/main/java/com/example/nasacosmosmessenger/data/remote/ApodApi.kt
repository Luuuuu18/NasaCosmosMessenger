package com.example.nasacosmosmessenger.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApodApi {

    private const val BASE_URL = "https://api.nasa.gov/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)   // 連線時間
        .readTimeout(30, TimeUnit.SECONDS)      // 讀取時間
        .writeTimeout(30, TimeUnit.SECONDS)     // 傳送時間
        .build()

    val service: ApodApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApodApiService::class.java)
    }
}