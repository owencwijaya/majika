package com.example.majika.model

import com.google.gson.annotations.SerializedName

data class Menu(

	@SerializedName("sold")
	val sold: Int? = null,

	@SerializedName("price")
	val price: Int? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("currency")
	val currency: String? = null,

	@SerializedName("type")
	val type: String? = null
)

