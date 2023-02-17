package com.example.majika.ui.payment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.majika.DataRepository
import com.example.majika.model.PaymentStatus
import com.example.majika.utils.Payment
import com.example.majika.utils.RetrofitClient
import kotlinx.coroutines.*

class PaymentViewModel(private val repository: DataRepository): ViewModel() {
    var job: Job? = null
    var totalPrice: LiveData<Int> = repository.totalPrice.asLiveData()
    var paymentStatus = MutableLiveData<PaymentStatus>()

    fun getPaymentStatus(code: String){
        if (code.length != 32) paymentStatus.value = PaymentStatus("Code is invalid")
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getPaymentService.getPaymentStatus(code)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    paymentStatus.value = response.body()
                }
            }
        }
    }

    fun deleteCart() = viewModelScope.launch {
        repository.deleteCart()
    }
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