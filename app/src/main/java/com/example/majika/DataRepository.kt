package com.example.majika

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.majika.db.RoomConfig
import com.example.majika.db.dao.CartItemDao
import com.example.majika.db.entity.CartItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

// Kalo nambah dao, inject ke param, kalo ada get, get di awal
public class DataRepository(private val cartItemDao: CartItemDao) {
    val cartItems: Flow<List<CartItemEntity>> = cartItemDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cartItem: CartItemEntity){
        cartItemDao.insert(cartItem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(cartItem: CartItemEntity){
        cartItemDao.delete(cartItem)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getEntity(cartItem: CartItemEntity) : LiveData<List<CartItemEntity>>{
        return cartItemDao.getEntity(cartItem.name, cartItem.price, cartItem.currency)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateQuantity(cartItem: CartItemEntity, quantity: Int){
        cartItemDao.updateQuantity(quantity, cartItem.name, cartItem.price, cartItem.currency)
    }
}