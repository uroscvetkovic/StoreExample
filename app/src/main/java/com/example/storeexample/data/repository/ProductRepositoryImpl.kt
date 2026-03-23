package com.example.storeexample.data.repository

import com.example.storeexample.data.remote.api.ProductApiService
import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.remote.model.ProductsResponse
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(limit: Int, skip: Int): ProductsResponse =
        api.getProducts(limit, skip)

    override suspend fun getProduct(id: Int): Product =
        api.getProduct(id)
}
