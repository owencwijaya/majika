package com.example.majika.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.majika.model.CartItem
import com.example.majika.model.dao.CartItemDao

@Database(entities = [CartItem::class], version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun userDao(): CartItemDao
}