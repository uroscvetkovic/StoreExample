package com.example.storeexample.data.remote.api

import com.example.storeexample.data.remote.model.Product
import com.example.storeexample.data.remote.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
}
