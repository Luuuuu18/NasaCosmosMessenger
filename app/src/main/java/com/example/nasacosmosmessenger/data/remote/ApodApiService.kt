package com.example.nasacosmosmessenger.data.remote


import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApiService {

    @GET("planetary/apod")
    suspend fun getTodayApod(
        @Query("api_key") apiKey: String
    ): ApodResponse

    @GET("planetary/apod")
    suspend fun getApodByDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): ApodResponse
}