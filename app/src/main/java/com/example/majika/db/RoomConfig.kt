package com.example.majika.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.majika.db.entity.CartItemEntity
import com.example.majika.db.dao.CartItemDao
import kotlinx.coroutines.CoroutineScope

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class RoomConfig:RoomDatabase(){
    abstract fun cartItemDao(): CartItemDao
    private class RoomConfigCallback(val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
//            INSTANCE?.let {database -> scope.launch {
//                // Populate here if needed
//                var cartItemDao = database.cartItemDao()
//                var cartItemEntity = CartItemEntity(name = "Makanan 1", price = 50000, quantity = 3, currency = "Rp")
//                cartItemDao.insert(cartItemEntity)
//                cartItemEntity = CartItemEntity(name = "Makanan 2", price = 50000, quantity = 3, currency = "Rp")
//                cartItemDao.insert(cartItemEntity)
//            }}
        }
    }

    companion object {
        @Volatile
        private var INSTANCE : RoomConfig? = null
        private val DATABASE_NAME: String = "majika_db"

        fun getDatabase(context: Context, scope: CoroutineScope):RoomConfig{
            return INSTANCE ?: synchronized(this){
                val initInstance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomConfig::class.java,
                    this.DATABASE_NAME
                ).addCallback(RoomConfigCallback(scope))
                    .build()

                INSTANCE = initInstance

                initInstance
            }
        }


    }
}