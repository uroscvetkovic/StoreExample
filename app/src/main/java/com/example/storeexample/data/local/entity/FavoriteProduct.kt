package com.example.storeexample.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteProduct(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val thumbnail: String
)
