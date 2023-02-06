package com.example.majika.model

import com.google.gson.annotations.SerializedName

data class Branch(

	@SerializedName("address")
	val address: String? = null,

	@SerializedName("contact_person")
	val contactPerson: String? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("phone_number")
	val phoneNumber: String? = null,

	@SerializedName("popular_food")
	val popularFood: String? = null,

	@SerializedName("latitude")
	val latitude: Float? = null,

	@SerializedName("longitude")
	val longitude: Float? = null
)

