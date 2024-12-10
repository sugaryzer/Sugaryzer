package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class ScanResponse(

	@field:SerializedName("result")
	val result: ResultScan,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ResultScan(

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
