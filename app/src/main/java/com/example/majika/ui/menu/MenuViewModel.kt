package com.example.majika.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.majika.model.MenuList
import com.example.majika.utils.RetrofitClient
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {
    var job: Job? = null
    var foodList = MutableLiveData<MenuList>()
    var drinksList = MutableLiveData<MenuList>()

    fun getFood(){
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getMenuService.getFood()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    foodList.postValue(response.body())
                }
            }
        }
    }

    fun getDrinks(){
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.getMenuService.getDrink()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    drinksList.postValue(response.body())
                }
            }
        }
    }


}