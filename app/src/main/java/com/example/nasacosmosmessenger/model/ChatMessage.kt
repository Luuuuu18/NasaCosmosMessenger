package com.example.nasacosmosmessenger.model


data class ChatMessage(
    val id: String,
    val content: String?,
    val imageUrl: String?,
    val date: String?,
    val isUser: Boolean,
    val timestamp: Long
)