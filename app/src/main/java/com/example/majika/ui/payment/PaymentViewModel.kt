package com.example.majika.ui.payment

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
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
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getPaymentService.getPaymentStatus(code)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    paymentStatus.value = response.body()
                }
            }
        }
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