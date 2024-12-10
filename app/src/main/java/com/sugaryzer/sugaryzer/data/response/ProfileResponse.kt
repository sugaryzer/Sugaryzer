package com.sugaryzer.sugaryzer.data.response

data class ProfileResponse(
	val result: ResultProfile? = null,
	val error: Boolean? = null,
	val message: String? = null
)

data class ResultProfile(
	val name: String,
	val weight: Int,
	val age: Int,
	val height: Int
)

