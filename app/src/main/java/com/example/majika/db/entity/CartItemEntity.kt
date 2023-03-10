package com.example.majika.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_item")
class CartItemEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "quantity") val quantity: Int
)
