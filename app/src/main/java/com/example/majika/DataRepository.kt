package com.example.majika

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.majika.db.RoomConfig
import com.example.majika.db.dao.CartItemDao
import com.example.majika.db.entity.CartItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


// Data repository is used when you need to access the database.
// So you cannot access the Dao directly, you need to access it through data Repository.
// It's just good practice, nothing else.
// Kalo nambah dao, inject ke param, kalo ada get, get di awal
public class DataRepository(private val cartItemDao: CartItemDao) {
    val cartItems: Flow<List<CartItemEntity>> = cartItemDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cartItem: CartItemEntity){
        cartItemDao.insert(cartItem)
    }

    // TODO Tambah fungsi lain
}