package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class AnalysisResponse(

	@field:SerializedName("result")
	val result: ResultAnalysis? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserProfile(

	@field:SerializedName("name")
	val name: String? = null
)

data class DataItemAnalysis(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("totalConsume")
	val totalConsume: Int? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("userProfile")
	val userProfile: UserProfile? = null
)

data class PagingAnalysis(

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("total_page")
	val totalPage: Int? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class ResultAnalysis(

	@field:SerializedName("data")
	val data: List<DataItemAnalysis>? = null,

	@field:SerializedName("paging")
	val paging: PagingAnalysis? = null
)
