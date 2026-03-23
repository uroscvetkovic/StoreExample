package com.example.storeexample.data.repository

import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.remote.model.ProductsResponse

interface ProductRepository {
    suspend fun getProducts(limit: Int, skip: Int): ProductsResponse
    suspend fun getProduct(id: Int): Product
}
