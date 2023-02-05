package com.example.majika.model

import com.google.gson.annotations.SerializedName

data class BranchList(

    @SerializedName("data")
    val branchList: List<Branch>? = null,

    @SerializedName("size")
    val size: Int? = null

)
