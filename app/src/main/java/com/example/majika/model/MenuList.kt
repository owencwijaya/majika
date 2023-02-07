package com.example.majika.model

import com.google.gson.annotations.SerializedName

data class MenuList(

    @SerializedName("data")
    val menuList: List<Menu>? = null,

    @SerializedName("size")
    val size: Int? = null

)