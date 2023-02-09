package com.example.majika.db.dao;

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.majika.db.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_item")
    fun getAll(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_item WHERE name LIKE :name AND price = :price AND currency LIKE :currency LIMIT 1")
    fun getEntity(name: String, price: Int, currency: String) : LiveData<List<CartItemEntity>>

    @Query("UPDATE cart_item SET quantity = :quantity WHERE name LIKE :name AND price = :price AND currency LIKE :currency")
    suspend fun updateQuantity(quantity: Int, name: String, price: Int, currency: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cartItemEntity: CartItemEntity)

    @Delete
    suspend fun delete(cartItemEntity: CartItemEntity)
}
