package com.example.majika

import android.app.Application
import com.example.majika.db.RoomConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MajikaApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val db by lazy{
        RoomConfig.getDatabase(this, applicationScope)
    }
    val repository by lazy { DataRepository(db.cartItemDao()) }
}