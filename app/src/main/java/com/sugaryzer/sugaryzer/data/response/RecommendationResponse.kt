package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("result")
	val result: ResultRecommendation,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class AltProduct(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("amountOfSugar")
	val amountOfSugar: Int,

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String? = null
)

data class ResultRecommendation(

	@field:SerializedName("data")
	val data: List<DataItemRecommendation>? = null,

	@field:SerializedName("paging")
	val paging: Paging? = null
)

data class DataItemRecommendation(

	@field:SerializedName("sugarDifference")
	val sugarDifference: Int? = null,

	@field:SerializedName("altProductId")
	val altProductId: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("altProduct")
	val altProduct: AltProduct
)

data class Paging(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_page")
	val totalPage: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)
