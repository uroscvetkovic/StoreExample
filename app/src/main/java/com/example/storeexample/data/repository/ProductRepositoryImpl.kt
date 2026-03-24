package com.example.storeexample.data.repository

import com.example.storeexample.data.local.dao.FavoriteProductDao
import com.example.storeexample.data.local.entity.FavoriteProduct
import com.example.storeexample.data.remote.api.ProductApiService
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.remote.model.ProductsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService,
    private val favoriteDao: FavoriteProductDao
) : ProductRepository {

    override suspend fun getProducts(limit: Int, skip: Int): ProductsResponse =
        api.getProducts(limit, skip)

    override suspend fun getProduct(id: Int): Product =
        api.getProduct(id)

    override fun getFavorites(): Flow<List<FavoriteProduct>> = favoriteDao.getAll()

    override fun isFavorite(id: Int): Flow<Boolean> = favoriteDao.isFavorite(id)

    override suspend fun addFavorite(product: FavoriteProduct) = favoriteDao.insert(product)

    override suspend fun removeFavorite(product: FavoriteProduct) = favoriteDao.delete(product)
}
