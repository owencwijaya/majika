package com.example.majika.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.majika.DataRepository

class PaymentViewModel(private val repository: DataRepository): ViewModel() {
    var totalPrice: LiveData<Int> = repository.totalPrice.asLiveData()
}

class PaymentViewModelFactory(private val repository: DataRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras) : T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaymentViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel Class")
    }
}