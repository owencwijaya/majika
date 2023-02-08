package com.example.majika.model.dao;

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.majika.model.CartItem


@Dao
interface CartItemDao {
    @Query("SELECT * FROM cartItem")
    fun getAll(): List<CartItem>

    @Insert
    fun insert(vararg cartItem: CartItem)

    @Delete
    fun delete(cartItem: CartItem)
}
