package com.example.nasacosmosmessenger.data.remote


data class ApodResponse(
    val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val media_type: String
)