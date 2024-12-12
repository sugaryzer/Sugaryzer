package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class ConsumeResponse(

	@field:SerializedName("result")
	val result: ResultConsume,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class UserProfileConsume(

	@field:SerializedName("name")
	val name: String
)

data class ResultConsume(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("totalConsume")
	val totalConsume: Int,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("userProfile")
	val userProfile: UserProfileConsume
)
