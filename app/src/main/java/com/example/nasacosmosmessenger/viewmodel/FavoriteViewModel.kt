package com.example.nasacosmosmessenger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasacosmosmessenger.data.local.FavoriteEntity
import com.example.nasacosmosmessenger.data.repository.FavoriteRepository
import com.example.nasacosmosmessenger.model.ChatMessage
import kotlinx.coroutines.launch
import java.util.UUID

class FavoriteViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {

    val favorites = repository.favorites

    fun addFavorite(message: ChatMessage) {

        if (message.imageUrl == null) return

        viewModelScope.launch {
            repository.addFavorite(
                FavoriteEntity(
                    date = message.date ?: UUID.randomUUID().toString(),
                    title = message.content ?: "",
                    explanation = "",
                    url = message.imageUrl
                )
            )
        }
    }

    fun deleteFavorite(item: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(item)
        }
    }
}