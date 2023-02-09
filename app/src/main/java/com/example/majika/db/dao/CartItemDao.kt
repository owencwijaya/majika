package com.example.majika.db.dao;

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.majika.db.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_item")
    fun getAll(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cartItemEntity: CartItemEntity)

    @Delete
    fun delete(cartItemEntity: CartItemEntity)
}
