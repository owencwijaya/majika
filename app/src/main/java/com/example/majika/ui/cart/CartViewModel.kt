package com.example.majika.ui.cart

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.majika.DataRepository
import com.example.majika.db.RoomConfig
import com.example.majika.db.dao.CartItemDao
import com.example.majika.db.entity.CartItemEntity
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.majika.MajikaApplication

class CartViewModel(private val repository: DataRepository) : ViewModel() {
    var cartItems: LiveData<List<CartItemEntity>> = repository.cartItems.asLiveData()

    // Contoh kode insert or any operation to db
    fun insert(cartItem: CartItemEntity) = viewModelScope.launch {
        repository.insert(cartItem)
    }

    fun delete(cartItem: CartItemEntity) = viewModelScope.launch {
        repository.delete(cartItem)
    }
    fun getEntity(cartItem: CartItemEntity) : LiveData<List<CartItemEntity>> {
        var temp: LiveData<List<CartItemEntity>> = MutableLiveData()
        viewModelScope.launch {
           temp = repository.getEntity(cartItem)
        }
        return temp
    }
    fun updateQuantity(cartItem: CartItemEntity, quantity: Int) = viewModelScope.launch {
        repository.updateQuantity(cartItem, quantity)
    }
}

class CartViewModelFactory(private val repository: DataRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras) : T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}