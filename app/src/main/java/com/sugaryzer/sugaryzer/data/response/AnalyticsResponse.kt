package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class AnalyticsResponse(

	@field:SerializedName("result")
	val result: ResultAnalytics,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ResultAnalytics(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("advice")
	val advice: String,

	@field:SerializedName("category")
	val category: String
)
