package com.example.majika.ui.branch

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.majika.model.Branch
import com.example.majika.model.BranchList
import com.example.majika.utils.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BranchViewModel : ViewModel() {

    private val _branchList = MutableLiveData<BranchList>().apply {
        NetworkConfig().getService().getBranch().enqueue(
            object: Callback<BranchList> {
                override fun onFailure(call : Call<BranchList>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<BranchList>, response: Response<BranchList>) {
                    if (response.isSuccessful) {
                        System.out.println(response.body())
                        value = response.body()
                    }
                }
            }
        )
    }
    val branchList: LiveData<BranchList> = _branchList
}