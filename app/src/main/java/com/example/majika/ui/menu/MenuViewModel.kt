package com.example.majika.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.majika.model.MenuList
import com.example.majika.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {

    var _foodList = MutableLiveData<MenuList>().apply {
        RetrofitClient.getMenuService.getFood().enqueue(
            object: Callback<MenuList> {
                override fun onFailure(call : Call<MenuList>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<MenuList>, response: Response<MenuList>) {
                    if (response.isSuccessful) {
                        System.out.println(response.body())
                        value = response.body()
                    }
                }
            }
        )
    }

    val foodList: LiveData<MenuList> = _foodList

    var _drinksList = MutableLiveData<MenuList>().apply {
        RetrofitClient.getMenuService.getDrink().enqueue(
            object: Callback<MenuList> {
                override fun onFailure(call : Call<MenuList>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<MenuList>, response: Response<MenuList>) {
                    if (response.isSuccessful) {
                        System.out.println(response.body())
                        value = response.body()
                    }
                }
            }
        )
    }

    val drinksList: LiveData<MenuList> = _drinksList
}