package com.sugaryzer.sugaryzer.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("result")
	val result: ResultRegister,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ResultRegister(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)
