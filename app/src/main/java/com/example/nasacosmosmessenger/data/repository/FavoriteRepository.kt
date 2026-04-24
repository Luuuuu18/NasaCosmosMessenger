package com.example.nasacosmosmessenger.data.repository

import com.example.nasacosmosmessenger.data.local.FavoriteDao
import com.example.nasacosmosmessenger.data.local.FavoriteEntity

class FavoriteRepository(private val dao: FavoriteDao) {

    val favorites = dao.getAllFavorites()

    suspend fun addFavorite(item: FavoriteEntity) {
        dao.insertFavorite(item)
    }

    suspend fun deleteFavorite(item: FavoriteEntity) {
        dao.deleteFavorite(item)
    }
}