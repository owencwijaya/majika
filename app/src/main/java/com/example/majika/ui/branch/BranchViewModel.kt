package com.example.majika.ui.branch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.majika.model.BranchList
import com.example.majika.utils.RetrofitClient
import kotlinx.coroutines.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BranchViewModel : ViewModel() {
    var job: Job? = null
    val branchList = MutableLiveData<BranchList>()
    fun getBranches(){
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getBranchService.getAll()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    branchList.postValue(response.body())
                }
            }
        }
    }
}