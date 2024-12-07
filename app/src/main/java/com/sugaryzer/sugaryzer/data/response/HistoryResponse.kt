package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
	@field:SerializedName("result")
	val result: ResultHistory? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ResultHistory(
	@field:SerializedName("data")
	val data: List<DataItemHistory>? = null,

	@field:SerializedName("paging")
	val paging: Paging? = null
)

data class DataItemHistory(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("product")
	val product: ProductHistory? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null
)

data class ProductHistory(
	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("amountOfSugar")
	val amountOfSugar: Int? = null
)

data class Paging(
	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_page")
	val totalPage: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)