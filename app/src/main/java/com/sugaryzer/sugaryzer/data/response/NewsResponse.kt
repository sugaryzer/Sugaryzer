package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("result")
	val result: ResultNews? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ResultNews(

	@field:SerializedName("data")
	val data: List<DataItemNews>? = null,

	@field:SerializedName("paging")
	val paging: PagingNews? = null
)

data class PagingNews(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_page")
	val totalPage: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class DataItemNews(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
