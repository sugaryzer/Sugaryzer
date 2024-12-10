package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class ScannedResponse(

	@field:SerializedName("result")
	val result: ResultScanned,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ProductScanned(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("amountOfSugar")
	val amountOfSugar: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class ResultScanned(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("product")
	val product: ProductScanned,

	@field:SerializedName("productId")
	val productId: Int,

	@field:SerializedName("sugarConsume")
	val sugarConsume: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: String
)
