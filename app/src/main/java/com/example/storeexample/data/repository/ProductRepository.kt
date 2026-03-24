package com.example.storeexample.data.repository

import com.example.storeexample.data.local.entity.FavoriteProduct
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.remote.model.ProductsResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(limit: Int, skip: Int): ProductsResponse
    suspend fun getProduct(id: Int): Product
    fun getFavorites(): Flow<List<FavoriteProduct>>
    fun isFavorite(id: Int): Flow<Boolean>
    suspend fun addFavorite(product: FavoriteProduct)
    suspend fun removeFavorite(product: FavoriteProduct)
}
