package com.example.majika.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.majika.model.CartItem
import com.example.majika.model.dao.CartItemDao

@Database(entities = [CartItem::class], version = 1)
abstract class RoomConfig:RoomDatabase(){
    abstract fun userDao(): CartItemDao
}